CC = gcc

CFLAGS += -I./curl/include/curl

LDFLAGS = -L./curl/lib

LINK := -lcurl -lm

SRC_FILES := ./cJSON/cJSON.c \
             ./get_weather.c

APP = get_weather

get_weather:
	$(CC) $(SRC_FILES) $(LDFLAGS) -o $(APP) $(LINK)

clean:
	rm $(APP)
