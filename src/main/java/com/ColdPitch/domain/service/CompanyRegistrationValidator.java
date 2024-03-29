package com.ColdPitch.domain.service;

import com.ColdPitch.config.okHttp.ApiConfig;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationDto;
import com.ColdPitch.domain.entity.dto.companyRegistraion.CompanyRegistrationValidationDto;
import com.ColdPitch.exception.CustomException;
import com.ColdPitch.exception.handler.ErrorCode;
import com.ColdPitch.utils.ServerUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRegistrationValidator {

    private final String API_URL = "http://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=";
    private final ApiConfig apiConfig;
    private final OkHttpClient client;

    public boolean validateCompanyRegistration(CompanyRegistrationValidationDto validationDto) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("businesses", gson.toJsonTree(Collections.singletonList(validationDto)));
        String json = gson.toJson(jsonObject);

        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .header("Access-Control-Request-Method", "POST")
                .header("Origin", ServerUtil.getCurrentBaseUrl())
                .url(API_URL + apiConfig.getCompanyServiceKey())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CustomException(ErrorCode.COMPANY_EXTERNAL_API_ERROR);
            }

            String responseBody = response.body().string();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
            if (responseJson.has("data")) {
                Type listType = new TypeToken<ArrayList<CompanyRegistrationDto>>() {
                }.getType();
                List<CompanyRegistrationDto> dtoList = gson.fromJson(responseJson.get("data"), listType);

                // valid가 01이면 성공, 02면 실패
                return dtoList.stream().allMatch(dto -> "01".equals(dto.getValid()));
            }
            return false;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.COMPANY_EXTERNAL_API_ERROR);
        }
    }
}