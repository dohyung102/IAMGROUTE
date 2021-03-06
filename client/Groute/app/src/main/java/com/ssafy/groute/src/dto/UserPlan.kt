package com.ssafy.groute.src.dto

data class UserPlan(
    val areaId: Int,
    val description: String?,
    val endDate: String,
    var heartCnt: Int,
    val id: Int,
    val isPublic: String,
    val rate: Double,
    val startDate: String,
    val themeIdList: List<Int>,
    val title: String,
    val totalDate: Int,
    val userId: String,
    val reviewCnt: Int
){
    constructor(areaId:Int, description: String,endDate: String,isPublic: String,startDate: String,title: String,totalDate: Int,userId: String)
            :this(areaId, description, endDate, 0, 0, isPublic, 0.0, startDate,
        listOf(), title, totalDate, userId, 0)

    constructor(id: Int,userId: String,title: String,description: String,startDate: String,endDate: String,totalDate: Int,isPublic: String,rate: Double,heartCnt: Int,areaId: Int, themeIdList: List<Int>, reviewCnt: Int)
        :this(areaId, description, endDate, heartCnt, id, isPublic, rate, startDate, themeIdList, title, totalDate, userId, reviewCnt)

    constructor() : this(0, null, "", 0, 0, "", 0.0, "", listOf(), "", 0, "", 0)
}