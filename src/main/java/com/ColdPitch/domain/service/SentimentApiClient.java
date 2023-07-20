package com.ColdPitch.domain.service;

import com.ColdPitch.config.okHttp.ApiConfig;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentRequestDto;
import com.ColdPitch.domain.entity.dto.sentiment.SentimentResponseDto;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SentimentApiClient {
    private final String API_URL = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";
    private final ApiConfig apiConfig;
    private final OkHttpClient client;
    private final Gson gson;

    public SentimentResponseDto callSentimentApi(SentimentRequestDto requestDto) {
        if (requestDto == null || requestDto.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.SENTIMENT_EMPTY_VALUE);
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String json = gson.toJson(requestDto);

        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .header("X-NCP-APIGW-API-KEY-ID", apiConfig.getSentimentKeyId())
                .header("X-NCP-APIGW-API-KEY", apiConfig.getSentimentKey())
                .header("Content-Type", "application/json")
                .url(API_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CustomException(ErrorCode.SENTIMENT_EXTERNAL_API_ERROR);
            }

            String responseBody = response.body().string();
            return gson.fromJson(responseBody, SentimentResponseDto.class);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.SENTIMENT_EXTERNAL_API_ERROR);
        } catch (NullPointerException e) {
            throw new CustomException(ErrorCode.SENTIMENT_BAD_RESPONSE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SENTIMENT_INTERNAL_SERVER_ERROR);
        }
    }
}
