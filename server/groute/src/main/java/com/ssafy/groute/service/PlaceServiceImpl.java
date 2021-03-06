package com.ssafy.groute.service;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.dto.PlaceLike;
import com.ssafy.groute.mapper.PlaceMapper;
import com.ssafy.groute.mapper.PlaceReviewMapper;
import com.ssafy.groute.mapper.RouteDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {
    @Autowired
    PlaceMapper placeMapper;
    @Autowired
    RouteDetailMapper routeDetailMapper;
    @Autowired
    PlaceReviewMapper placeReviewMapper;

    @Override
    public void insertPlace(Place place) throws Exception {
        placeMapper.insertPlace(place);
    }

    @Override
    public Place selectPlace(int id) throws Exception {
        return placeMapper.selectPlace(id);
    }

    @Override
    public List<Place> selectAllPlace() throws Exception {
        return placeMapper.selectAllPlace();
    }

    @Override
    public void deletePlace(int id) throws Exception {
        routeDetailMapper.deleteAllRouteDetailByPlaceId(id);
        placeMapper.deletePlaceLikeByPlaceId(id);
        placeReviewMapper.deletePlaceReviewByPlaceId(id);
        placeMapper.deletePlace(id);
    }

    @Override
    public void updatePlace(Place place) throws Exception {
        placeMapper.updatePlace(place);
    }

    @Override
    public void likePlace(PlaceLike placeLike) throws Exception {
        PlaceLike p = placeMapper.isLike(placeLike);
        Place place = placeMapper.selectPlace(placeLike.getPlaceId());
        if(p==null) {
            placeMapper.likePlace(placeLike);
            place.setHeartCnt(place.getHeartCnt()+1);
            placeMapper.updatePlace(place);
        }else{
            placeMapper.unLikePlace(p.getId());
            place.setHeartCnt(place.getHeartCnt()-1);
            placeMapper.updatePlace(place);
        }
    }

    @Override
    public PlaceLike isLike(PlaceLike placeLike) throws Exception {
        return placeMapper.isLike(placeLike);
    }

    @Override
    public List<Place> bestPlace() throws Exception {
        return placeMapper.bestPlace();
    }

    @Override
    public List<Place> selectAllPlaceIdByUserId(String userId) throws Exception {
        return placeMapper.selectAllPlaceIdByUserId(userId);
    }
}
