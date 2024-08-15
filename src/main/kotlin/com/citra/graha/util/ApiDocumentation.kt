package com.citra.graha.util

object ApiDocumentation {
    object Request {
        const val ADD_SERVICE_REQUEST_EXAMPLE = """
            {
                "serviceName": "Buat Kos",
                "serviceDescription": "Jasa Buat kos"
            }
        """
        const val ADD_PRODUCT_REQUEST_EXAMPLE = """
            {
                "productAddress": "123 Main St",
                "productBedroom": 3,
                "propertyId": 1,
                "productDescription": "A beautiful home with 3 bedrooms",
                "productBathroom": 2,
                "productLuasTanah": 120.0,
                "productLuasBangunan": 80.0,
                "productElectricity": 2200,
                "statusId": 1
            }
        """
        const val UPDATE_PRODUCT_REQUEST_EXAMPLE = """
            {
                "id": 1,
                "productAddress": "123 Main St",
                "productBedroom": 3,
                "propertyId": 1,
                "productDescription": "A beautiful home with 3 bedrooms",
                "productBathroom": 2,
                "productLuasTanah": 120.0,
                "productLuasBangunan": 80.0,
                "productElectricity": 2200,
                "statusId": 1,
                "productVisitCount": 5
            }
        """
        const val UPDATE_PRODUCT_STATUS_REQUEST_EXAMPLE = """
            {
                "id": 1,
                "statusId": 1
            }
        """
        const val UPDATE_PRODUCT_SOME_FIELDS_REQUEST_EXAMPLE = """
            {
                "id": 1,
                "productDescription": "A beautiful home with 3 bedrooms",
                "productElectricity": 2200,
                "statusId": 1,
                "productVisitCount": 5
            }
        """
        const val UPDATE_PROPERTY_TYPE_REQUEST_EXAMPLE = """
            {
                "propertyId": 1,
                "propertyName": "Test"
            }
        """
        const val REGISTER_USER_REQUEST_EXAMPLE_WITH_STATUS = """
            {
                "username": "daniel",
                "email": "daniel@gmail.com",
                "password": "daniel123",
                "status": "01",
                "roleId": 1
            }
        """
        const val REGISTER_USER_REQUEST_EXAMPLE_WITHOUT_STATUS = """
            {
                "username": "daniel",
                "email": "daniel@gmail.com",
                "password": "daniel123",
                "roleId": 1
            }
        """
        const val LOGIN_USER_REQUEST_EXAMPLE = """
            {
                "username": "daniel",
                "password": "daniel123"
            }
        """
        const val UPDATE_USER_REQUEST_USERNAME = """
            {
                "id": 1,
                "username": "danielupdate"
            }
        """
        const val UPDATE_USER_REQUEST_PASSWORD = """
            {
                "id": 1,
                "password": "updatepassword"
            }
        """
        const val UPDATE_USER_REQUEST_ROLE_STATUS = """
            {
                "id": 1,
                "roleId": 1,
                "status": "00"
            }
        """
        const val ADD_WORK_EXP_REQUEST_EXAMPLE = """
            {
                "name" : "test",
                "description" : "test description",
                "serviceId": 1
            }
        """
        const val UPDATE_WORK_EXP_REQUEST_NAME = """
            {
                "id" : 1,
                "name" : "test update"
            }
        """
        const val UPDATE_WORK_EXP_REQUEST_DESCRIPTION = """
            {
                "id" : 1,
                "description" : "update description"
            }
        """
        const val UPDATE_WORK_EXP_REQUEST_NAME_SERVICE = """
            {
                "id" : 1,
                "name" : "update name",
                "serviceId" : 2
            }
        """
    }
}