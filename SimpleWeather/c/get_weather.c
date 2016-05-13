#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "utility/curl/include/curl/curl.h"
#include "utility/cJSON/cJSON.h"

int read_appid_from_file(char *filename, char *appid);
size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream);
int print_weather_data();

char weather_data[1024];

int main(int argc, char *argv[])
{
    char apikey[64] = "";

    CURL *curl;
    struct curl_slist *list = NULL;

    read_appid_from_file("./app_config.txt", apikey);

    curl_global_init(CURL_GLOBAL_ALL);
    curl=curl_easy_init();

    curl_easy_setopt(curl, CURLOPT_URL, "http://apis.baidu.com/apistore/weatherservice/cityid?cityid=101280601");

    curl_easy_setopt(curl, CURLOPT_HTTPGET, 1L);

    list = curl_slist_append(list, apikey);
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, list);

    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
    curl_easy_perform(curl);
    curl_easy_cleanup(curl);

    print_weather_data();

    exit(0);
}

int read_appid_from_file(char *filename, char *appid)
{
    FILE *fp;
    int i = 0;
    int c;

    if ((fp = fopen(filename, "r")) == NULL) {
        printf("Can't open the app_config file.\n");
        return -1;
    }

    while ((c = getc(fp)) != EOF) {
        appid[i] = (char)c;
        ++i;
    }
    appid[i] = '\0';

    fclose(fp);

    return 0;
}

size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream)
{
    int i = 0;
    char *sptr = (char *)ptr;

    while (i < size * nmemb) {
        weather_data[i] = sptr[i];
        ++i;
    }
    weather_data[i] = '\0';

    return 0;
}

int print_weather_data()
{
    cJSON *json, *json_object, *json_value;

	// printf("%s\n", weather_data);
    json = cJSON_Parse(weather_data);
    if (!json) {
        printf("cJSON parse error\n");
    } else {
        json_object = cJSON_GetObjectItem(json, "retData");
        if (json_object->type == cJSON_Object) {
			printf("---------深圳天气--------\n");
            json_value = cJSON_GetObjectItem(json_object, "weather");
            printf("%s ", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "temp");
            printf("%s℃\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "l_tmp");
            printf("最低温度：%s℃\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "h_tmp");
            printf("最高温度：%s℃\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "WD");
            printf("风向：%s\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "WS");
            printf("风力：%s\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "sunrise");
            printf("日出时间：%s\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "sunset");
            printf("日落时间：%s\n", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "date");
            printf("发布时间：%s ", json_value->valuestring);
            json_value = cJSON_GetObjectItem(json_object, "time");
            printf("%s\n", json_value->valuestring);
        }
        cJSON_Delete(json);
    }

    return 0;
}
