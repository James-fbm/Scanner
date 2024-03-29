#=======================================================================================================================
# Preamble
#=======================================================================================================================
cmake_minimum_required(VERSION 3.15 FATAL_ERROR)

set(CMAKE_CXX_VISIBILITY_PRESET hidden)
set(CMAKE_VISIBILITY_INLINES_HIDDEN YES)
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

#=======================================================================================================================
# Set C/C++ compiler version
#=======================================================================================================================
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(IGNORE_ME ${CMAKE_C_COMPILER}) # Suppress CMake warning message

#=======================================================================================================================
# Add build options
#=======================================================================================================================
option(OPENXLSX_COMPACT_MODE "Build library in compact mode (slower, but uses less memory)" OFF)
set(OPENXLSX_LIBRARY_TYPE "STATIC" CACHE STRING "Set the library type to SHARED or STATIC")

#=======================================================================================================================
# EXTERNAL LIBRARIES
#   Define external libraries used by OpenXLSX. The libraries (Zippy, PugiXML, and NoWide) are header-only, so
#   INTERFACE libraries should be defined.
#=======================================================================================================================
add_library(NoWide INTERFACE IMPORTED)
target_include_directories(NoWide SYSTEM INTERFACE $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}/external/nowide/>)

add_library(Zippy INTERFACE IMPORTED)
target_include_directories(Zippy SYSTEM INTERFACE $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}/external/zippy/>)

add_library(PugiXML INTERFACE IMPORTED)
target_include_directories(PugiXML SYSTEM INTERFACE $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}/external/pugixml/>)

if (${OPENXLSX_COMPACT_MODE})
    target_compile_definitions(PugiXML INTERFACE PUGIXML_COMPACT)
endif ()

#=======================================================================================================================
# COMPILER FEATURES
#   Some older C++17 compilers don't support the char_conv features. If the compiler doesn't support it,
#   a less optimal work-around will be used.
#=======================================================================================================================
include(CheckCXXSourceCompiles)
check_cxx_source_compiles("
                          #include <array>
                          #include <string>
                          #include <charconv>
                          
                          int main() {
                                  std::array<char, 7> str {};
                                  auto p = std::to_chars(str.data(), str.data() + str.size(), 12345).ptr;
                                  auto strResult = std::string(str.data(), p - str.data());
                                  
                                  unsigned long value = 0;
                                  std::from_chars(strResult.data(), strResult.data() + strResult.size(), value);

                                  return 0;
                          }" CHARCONV_RESULT)

if (CHARCONV_RESULT)
    add_compile_definitions(CHARCONV_ENABLED)
endif ()

#=======================================================================================================================
# PROJECT FILES
#   List of project source files
#=======================================================================================================================
set(OPENXLSX_SOURCES
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLCell.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLCellIterator.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLCellRange.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLCellReference.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLCellValue.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLColor.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLColumn.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLContentTypes.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLDateTime.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLDocument.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLFormula.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLProperties.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLRelationships.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLRow.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLRowData.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLSharedStrings.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLSheet.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLWorkbook.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLXmlData.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLXmlFile.cpp
        ${CMAKE_CURRENT_LIST_DIR}/sources/XLZipArchive.cpp
        )


#=======================================================================================================================
# STATIC AND SHARED LIBRARY
#   Check that the input is valid
#=======================================================================================================================
if(NOT ${OPENXLSX_LIBRARY_TYPE} STREQUAL "STATIC" AND NOT ${OPENXLSX_LIBRARY_TYPE} STREQUAL "SHARED")
    message( FATAL_ERROR "Invalid library type. Must be SHARED or STATIC." )
endif()

#=======================================================================================================================
# STATIC LIBRARY
#   Define the static library
#=======================================================================================================================
if (${OPENXLSX_LIBRARY_TYPE} STREQUAL "STATIC")
    add_library(OpenXLSX STATIC "")
    add_library(OpenXLSX::OpenXLSX ALIAS OpenXLSX)
    target_sources(OpenXLSX PRIVATE ${OPENXLSX_SOURCES})
    target_include_directories(OpenXLSX
            PUBLIC
            $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}>
            $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}/headers>
            $<BUILD_INTERFACE:${CMAKE_CURRENT_BINARY_DIR}>)     # For export header
    target_link_libraries(OpenXLSX
            PRIVATE
            $<BUILD_INTERFACE:Zippy>
            $<BUILD_INTERFACE:PugiXML>
            $<BUILD_INTERFACE:NoWide>)

    target_compile_definitions(OpenXLSX PUBLIC OPENXLSX_STATIC_DEFINE)

endif ()

#=======================================================================================================================
# SHARED LIBRARY
#   Define the shared library
#=======================================================================================================================
if (${OPENXLSX_LIBRARY_TYPE} STREQUAL "SHARED")
    add_library(OpenXLSX SHARED "")
    add_library(OpenXLSX::OpenXLSX ALIAS OpenXLSX)
    target_sources(OpenXLSX PRIVATE ${OPENXLSX_SOURCES})
    target_include_directories(OpenXLSX
            PUBLIC
            $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}>
            $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}/headers>
            $<BUILD_INTERFACE:${CMAKE_CURRENT_BINARY_DIR}>)     # For export header
    target_link_libraries(OpenXLSX
            PRIVATE
            $<BUILD_INTERFACE:Zippy>
            $<BUILD_INTERFACE:PugiXML>
            $<BUILD_INTERFACE:NoWide>)

    # Enable Link-Time Optimization (LTO)
    include(CheckIPOSupported)
    check_ipo_supported(RESULT result OUTPUT output)
    if (result)
        set_property(TARGET OpenXLSX PROPERTY INTERPROCEDURAL_OPTIMIZATION TRUE)
    endif ()

endif()

# Generate export header
include(GenerateExportHeader)
generate_export_header(OpenXLSX
        BASE_NAME openxlsx
        EXPORT_FILE_NAME OpenXLSX-Exports.hpp
        EXPORT_MACRO_NAME OPENXLSX_EXPORT
        NO_EXPORT_MACRO_NAME OPENXLSX_HIDDEN)

#=======================================================================================================================
# COMPILER FLAGS
#   Set compiler debug flags for GCC, Clang and MSVC.
#=======================================================================================================================
list(APPEND OPENXLSX_DEBUG_FLAGS_GNU
        "-Wmisleading-indentation"
        "-Wduplicated-cond"
        "-Wduplicated-branches"
        "-Wlogical-op"
        "-Wnull-dereference")
list(APPEND OPENXLSX_RELEASE_FLAGS_GNU
        "-O3"
        "-march=native"
        "-flto"
        "-g0")
list(APPEND OPENXLSX_DEBUG_FLAGS_GNUCLANG
        "-Wall"
        "-Wextra"
        "-Wshadow"
        "-Wnon-virtual-dtor"
        "-Wold-style-cast"
        "-Wcast-align"
        "-Wunused"
        "-Woverloaded-virtual"
        "-Wpedantic"
        "-Wconversion"
        "-Wdouble-promotion"
        "-Wformat=2"
        "-Weffc++"
        "-Wno-unknown-pragmas")
list(APPEND OPENXLSX_RELEASE_FLAGS_GNUCLANG
        "-O3"
        "-flto"
        "-g0")
list(APPEND OPENXLSX_DEBUG_FLAGS_MSVC
        "/utf-8"
        "/permissive"
        "/W4"
        "/w14242"
        "/w14254"
        "/w14263"
        "/w14265"
        "/w14287"
        "/we4289"
        "/w14296"
        "/w14311"
        "/w14545"
        "/w14546"
        "/w14547"
        "/w14549"
        "/w14555"
        "/w14619"
        "/w14640"
        "/w14826"
        "/w14905"
        "/w14906"
        "/w14928"
        "/wd4251"
        "/wd4275")
list(APPEND OPENXLSX_RELEASE_FLAGS_MSVC
        "/utf-8"
        "/O2"
        "/GL"
        "/wd4251"
        "/wd4275")

if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    target_compile_options(OpenXLSX PRIVATE $<$<CONFIG:Debug>:${OPENXLSX_DEBUG_FLAGS_GNU}>)
    target_compile_options(OpenXLSX PRIVATE $<$<CONFIG:Release>:${OPENXLSX_RELEASE_FLAGS_GNU}>)

elseif (("${CMAKE_CXX_COMPILER_ID}" STREQUAL "Clang") OR
("${CMAKE_CXX_COMPILER_ID}" STREQUAL "AppleClang") OR
("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU"))
    target_compile_options(OpenXLSX PRIVATE $<$<CONFIG:Debug>:${OPENXLSX_DEBUG_FLAGS_GNUCLANG}>)
    target_compile_options(OpenXLSX PRIVATE $<$<CONFIG:Release>:${OPENXLSX_RELEASE_FLAGS_GNUCLANG}>)

elseif ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
    string(REGEX REPLACE "/W[3|4]" "" CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
    target_compile_options(OpenXLSX PRIVATE $<$<CONFIG:Debug>:${OPENXLSX_DEBUG_FLAGS_MSVC}>)
    target_compile_options(OpenXLSX PRIVATE $<$<CONFIG:Release>:${OPENXLSX_RELEASE_FLAGS_MSVC}>)
endif ()
