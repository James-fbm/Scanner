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

add_subdirectory(libxls)
add_subdirectory(OpenXLSX)

add_library(ExcelReader excelreader.cpp)
target_link_libraries(ExcelReader libxls)
target_link_libraries(ExcelReader OpenXLSX)

#=======================================================================================================================
# COMPILER FLAGS
#   Set compiler debug flags for GCC, Clang and MSVC.
#=======================================================================================================================
list(APPEND EXCELREADER_DEBUG_FLAGS_GNU
        "-Wmisleading-indentation"
        "-Wduplicated-cond"
        "-Wduplicated-branches"
        "-Wlogical-op"
        "-Wnull-dereference")
list(APPEND EXCELREADER_RELEASE_FLAGS_GNU
        "-O3"
        "-march=native"
        "-flto"
        "-g0")
list(APPEND EXCELREADER_DEBUG_FLAGS_GNUCLANG
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
list(APPEND EXCELREADER_RELEASE_FLAGS_GNUCLANG
        "-O3"
        "-flto"
        "-g0")
list(APPEND EXCELREADER_DEBUG_FLAGS_MSVC
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
list(APPEND EXCELREADER_RELEASE_FLAGS_MSVC
        "/utf-8"
        "/O2"
        "/GL"
        "/wd4251"
        "/wd4275")

if ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    target_compile_options(ExcelReader PRIVATE $<$<CONFIG:Debug>:${EXCELREADER_DEBUG_FLAGS_GNU}>)
    target_compile_options(ExcelReader PRIVATE $<$<CONFIG:Release>:${EXCELREADER_RELEASE_FLAGS_GNU}>)

elseif (("${CMAKE_CXX_COMPILER_ID}" STREQUAL "Clang") OR
("${CMAKE_CXX_COMPILER_ID}" STREQUAL "AppleClang") OR
("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU"))
    target_compile_options(ExcelReader PRIVATE $<$<CONFIG:Debug>:${EXCELREADER_DEBUG_FLAGS_GNUCLANG}>)
    target_compile_options(ExcelReader PRIVATE $<$<CONFIG:Release>:${EXCELREADER_RELEASE_FLAGS_GNUCLANG}>)

elseif ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "MSVC")
    string(REGEX REPLACE "/W[3|4]" "" CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
    target_compile_options(ExcelReader PRIVATE $<$<CONFIG:Debug>:${EXCELREADER_DEBUG_FLAGS_MSVC}>)
    target_compile_options(ExcelReader PRIVATE $<$<CONFIG:Release>:${EXCELREADER_RELEASE_FLAGS_MSVC}>)
endif ()
