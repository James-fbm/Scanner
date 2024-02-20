#ifdef __APPLE__
#include "unix/config-macos.h"
#elif defined(__sun)
#include "unix/config-solaris.h"
#elif defined(_WIN32)
#include "windows/config-windows.h"
#else
#include "unix/config-unix.h"
#endif
