CC	= g++
CFLAGS = -g `pkg-config --cflags opencv`
DEPS = utilities.h
DEPS_OBJS = utilities.o
TARGET1 = P1
TARGET2 = P2
TARGET3 = P3
TARGET4 = P4
TARGET5 = P5
TARGET6 = P6
OBJS1 = P1.o
OBJS2 = P2.o
OBJS3 = P3.o
OBJS4 = P4.o
OBJS5 = P5.o
OBJS6 = P6.o
LIBS	= `pkg-config --libs opencv`

all: $(TARGET1) $(TARGET2) $(TARGET3)

%.o: %.cpp $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

$(TARGET1): $(OBJS1) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS1) $(DEPS_OBJS) $(LIBS)

$(TARGET2): $(OBJS2) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS2) $(DEPS_OBJS) $(LIBS)

$(TARGET3): $(OBJS3) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS3) $(DEPS_OBJS) $(LIBS)

$(TARGET4): $(OBJS4) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS4) $(DEPS_OBJS) $(LIBS)

$(TARGET5): $(OBJS5) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS5) $(DEPS_OBJS) $(LIBS)

$(TARGET6): $(OBJS6) $(DEPS_OBJS)
	$(CC) -o $@ $(OBJS6) $(DEPS_OBJS) $(LIBS)

$(OBJS1): $(DEPS)

$(OBJS2): $(DEPS)

$(OBJS3): $(DEPS)

$(OBJS4): $(DEPS)

$(OBJS5): $(DEPS)

$(OBJS6): $(DEPS)

.SUFFIXES: .cpp .o

.cpp.o:
	$(CC) -c $(CFLAGS) $<

.PHONY: clean

clean:
	/bin/rm -f core *.o $(TARGET1) $(TARGET2) $(TARGET3) $(TARGET4) $(TARGET5) $(TARGET6)
