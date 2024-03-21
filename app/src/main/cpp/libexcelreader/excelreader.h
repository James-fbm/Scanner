#include "csvparser/csv.h"
#include "libxls/include/xls.h"
#include "OpenXLSX/OpenXLSX.hpp"

#include <vector>
#include <map>
#include <unordered_map>
#include <algorithm>
#include <string>
#include <cstdio>
#include <stdexcept>

// key: header that user selects as index columns
// value: all the row records of a certain index column set
using IndexRecord = std::map<std::vector<std::string>, std::vector<std::string>>;

std::vector<std::string> read_csvline(const char *, bool, const char *);
std::vector<std::string> read_csvheader(const char *);
IndexRecord read_csvrecord(const char *, const std::vector<int> &);
std::string to_csvline(std::vector<std::string>& record_array);