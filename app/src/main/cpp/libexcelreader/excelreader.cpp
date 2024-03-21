#include "excelreader.h"

std::vector<std::string>
read_csvline(const char *csv_line, const bool duplicate_suffix, const char *fill_nan) {
    if (csv_line == nullptr) {
        return std::vector<std::string>();
    }

    std::vector<std::string> line_element;
    std::string element_truncate;
    const char *pch_front = csv_line, *pch_current = csv_line;
    bool has_quotes = false;

    for (; *pch_current != '\0'; ++pch_current) {
        if (*pch_current == '"' && pch_current == pch_front) {
            // begin with character '"'
            has_quotes = true;
            continue;
        }
        if (has_quotes) {
            if (*pch_current == '"') {
                ++pch_current;
                if (*(pch_current) == '"') {
                    // character '"' occurs in pairs if not meet the end
                    element_truncate += '"';
                } else {
                    // meet the end
                    // *pch_current now may be ',' or '\0'
                    if (*pch_current != ',' && *pch_current != '\0') {
                        throw std::exception();
                    }

                    if (*pch_current == ',') {
                        has_quotes = false;
                    }

                    line_element.push_back(element_truncate);
                    element_truncate = "";
                    pch_front = pch_current + 1;
                }
            } else {
                element_truncate += *pch_current;
            }
        } else {
            // meet the end of a new element
            if (*pch_current == ',') {
                if (pch_current == pch_front) {
                    // the element is an empty string
                    if (fill_nan == nullptr) {
                        // default string to replace an empty string
                        line_element.push_back("");
                    } else {
                        line_element.push_back(fill_nan);
                    }
                } else {
                    line_element.push_back(element_truncate);
                }
                element_truncate = "";
                pch_front = pch_current + 1;
            } else {
                element_truncate += *pch_current;
            }
        }
    }

    // process the last element
    // only elements without quotes maybe left here
    if (!has_quotes) {
        if (pch_current == pch_front) {
            if (fill_nan == nullptr) {
                line_element.push_back("");
            } else {
                line_element.push_back(fill_nan);
            }
        } else {
            line_element.push_back(element_truncate);
        }
    }

    if (duplicate_suffix) {
        // record the duplication times of each element
        std::map<std::string, int> element_duplicate;
        for (auto &element: line_element) {
            if (element_duplicate.find(element) == element_duplicate.end()) {
                element_duplicate[element] = 1;
            } else {
                // if duplication occurs, set the value to negative in order to distinguish from non-duplicate elements
                if (element_duplicate[element] > 0) {
                    element_duplicate[element] = -element_duplicate[element];
                }
                element_duplicate[element] -= 1;
            }
        }

        // reversely traverse the element array to add suffix to duplicate elements
        for (auto riter_element = line_element.rbegin();
             riter_element != line_element.rend(); ++riter_element) {
            if (element_duplicate[*riter_element] < 0) {
                // suffix indicates the number of times the current element has duplicated, wrapped by a pair of bracket
                element_duplicate[*riter_element] += 1;
                *riter_element += ("(" + std::to_string(-element_duplicate[*riter_element] + 1) +
                                   ")");
            }
        }
    }
    return line_element;
}

std::vector<std::string> read_csvheader(const char *file_path) {
    if (file_path == nullptr) {
        return std::vector<std::string>();
    }

    csvio::LineReader line_reader(file_path);
    auto header_element = read_csvline(line_reader.next_line(), true, "Unnamed");
    return header_element;
}

IndexRecord read_csvrecord(const char *file_path, const std::vector<int> &index_id) {
    if (file_path == nullptr || index_id.size() == 0) {
        return IndexRecord();
    }

    IndexRecord index_record;

    csvio::LineReader line_reader(file_path);

    // ignore the header row and find out whether index_id are valid
    auto header_element = read_csvline(line_reader.next_line(), true, "");
    auto min_index_id = std::min_element(index_id.cbegin(), index_id.cend());
    auto max_index_id = std::max_element(index_id.cbegin(), index_id.cend());

    // invalid index_id exists
    if (*min_index_id < 0 || *max_index_id >= header_element.size()) {
        throw std::runtime_error("Invalid index mapping.");
    }

    int LINENO = 1;
    while (char *csv_line = line_reader.next_line()) {
        ++LINENO;
        std::vector<std::string> line_element;
        try {
            line_element = read_csvline(csv_line, false, "nan");
        } catch (const std::exception &e) {
            char error_string[64];
            sprintf(error_string, "LINE %d Invalid csv format.\n", LINENO);
            throw std::runtime_error(error_string);
        }

        // each row of the csv file should contain same number of elements
        // or we assert that the csv file is broken
        if (line_element.size() != header_element.size()) {
            char error_string[64];
            sprintf(error_string, "LINE %d Invalid csv format.\n", LINENO);
            throw std::runtime_error(error_string);
        }

        std::vector<std::string> index_element;

        // record the header of a certain id
        for (auto id: index_id) {
            index_element.push_back(line_element[id]);
        }

        auto index_line = to_csvline(index_element);

        if (index_record[index_line].size() == 6) {
            // only remain first three and last three elements
            index_record[index_line][3] = index_record[index_line][4];
            index_record[index_line][4] = index_record[index_line][5];
            index_record[index_line].pop_back();
        }
        index_record[index_line].push_back(to_csvline(line_element));
    }

    return index_record;
}

std::string to_csvline(std::vector<std::string>& record_array) {
    std::string record_line;
    bool quoting = false;
    for (auto record_element = record_array.cbegin() ; record_element != record_array.cend() ; ++record_element) {
        // check and process if the element has special characters
        if (record_element != record_array.cbegin()) {
            record_line += ',';
        }
        std::string element_process;
        for (auto ch: *record_element) {
            if (ch == '"' || ch == '\r' || ch == '\n' || ch == ',') {
                quoting = true;
                if (ch == '"') {
                    element_process += '"';
                }
            }
            element_process += ch;
        }
        if (quoting) {
            element_process.insert(0, "\"").append("\"");
            quoting = false;
        }
        record_line += element_process;
    }

    return record_line;
}