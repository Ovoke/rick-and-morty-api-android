package com.urban.androidhomework.repository;

import com.urban.androidhomework.model.Character;
import com.urban.androidhomework.model.CharacterData;
import com.urban.androidhomework.model.Location;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkApi {

    @GET("character/")
    Single<Response<Character>> getAllCharacters();

    @GET("character/{id}")
    Single<Response<CharacterData>> getCharacter(
            @Path("id") Integer id
    );

    @GET("character/{ids}")
    Single<Response<List<CharacterData>>> getLocationResidents(
            @Path("ids") List<Integer> ids
    );

    @GET("location/{id}")
    Single<Response<Location>> getLocation(
            @Path("id") Integer ids
    );
}
