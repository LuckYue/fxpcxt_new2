package com.example.a1917.fxpcxt_new.route;

public class Routs {
    /**
     * Authorization
     */
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    /**
     * Authorization
     */
    public static final String AUTHORIZATION_HEADER_NAME_PREFIX = "Bearer ";
    public static final String BASE_URL = "http://192.168.43.200:7001";
    public static final String LOGIN_URL = BASE_URL + "/userInfo/login";
    public static final String GET_ALL_USER_INFO = BASE_URL + "/userInfo/selectAll";
    public static final String SAVE_USER = BASE_URL + "/userInfo/save";

    public static final String GET_ALL_ENTERPRISE = BASE_URL + "/enterprise/selectAllEnterprise";
    public static final String GET_HAZARD_TYPE_INFO = BASE_URL + "/hazardclearancerecords/getInfoByType";
    public static final String SELECT_HAZARD_BY_ENTERPRISEANDTYPE = BASE_URL + "/hazardclearancerecords/selectRecordsByEnterpriseId";
    public static final String HAZARD_EXAMINE = BASE_URL + "/hazardclearancerecords/Recheck";

    public static final String SELECT_HAZARD_TYPE = BASE_URL + "/IndustryAndHazardType/selectAllByIndustryName";
    public static final String UPLOAD_FILES = BASE_URL + "/file/import";
}

