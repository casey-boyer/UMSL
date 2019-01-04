CC	= g++
CFLAGS = -g `pkg-config --cflags opencv`
DEPS = utilities.h
DEPS_OBJS = utilities.o
TARGET = ass5
OBJS = ass5.o
LIBS	= `pkg-config --libs opencv`

all: $(TARGET)

%.o: %.cpp $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

$(TARGET): $(OBJS) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS) $(DEPS_OBJS) $(LIBS)

$(OBJS): $(DEPS)

.SUFFIXES: .cpp .o

.cpp.o:
	$(CC) -c $(CFLAGS) $<

.PHONY: clean

clean:
	/bin/rm -f core *.o $(TARGET)
