CC	= g++
CFLAGS = -g `pkg-config --cflags opencv`
DEPS = utilities.h
TARGET6	= P6
TARGET5 = P5
TARGET4 = P4
TARGET3 = P3
TARGET2 = P2
TARGET1 = P1
OBJS6	= P6.o
OBJS5 = P5.o
OBJS4 = P4.o
OBJS3 = P3.o
OBJS2 = P2.o
OBJS1 = P1.o
LIBS	= `pkg-config --libs opencv`

all: $(TARGET6) $(TARGET5) $(TARGET4) $(TARGET3) $(TARGET2) $(TARGET1)

$(TARGET6): $(OBJS6)
	$(CC) -o $@ $(OBJS6) $(LIBS)

$(TARGET5): $(OBJS5)
	$(CC) -o $@ $(OBJS5) $(LIBS)

$(TARGET4): $(OBJS4)
	$(CC) -o $@ $(OBJS4) $(LIBS)

$(TARGET3): $(OBJS3)
	$(CC) -o $@ $(OBJS3) $(LIBS)

$(TARGET2): $(OBJS2)
	$(CC) -o $@ $(OBJS2) $(LIBS)

$(TARGET1): $(OBJS1)
	$(CC) -o $@ $(OBJS1) $(LIBS)

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
	/bin/rm -f core *.o $(TARGET6) $(TARGET5) $(TARGET4) $(TARGET3) $(TARGET2) $(TARGET1)
