CC = gcc

CFLAGS += -I./utility/curl/include/curl \
          -I./utility/cJSON

LDFLAGS = -L./curl/lib

LINK := -lcurl -lm

SRC_FILES := ./utility/cJSON/cJSON.c \
             ./get_weather.c

APP = get_weather

get_weather:
	$(CC) $(SRC_FILES) $(LDFLAGS) -o $(APP) $(LINK)

clean:
	rm $(APP)
