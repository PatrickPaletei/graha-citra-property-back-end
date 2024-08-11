package com.citra.graha.service

import com.citra.graha.dto.DeletePhotoDto
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.dto.response.DeleteResponse
import com.citra.graha.dto.response.PhotoResponse
import com.citra.graha.dto.response.WorkExpPhotoResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.MstWorkExperience
import com.citra.graha.entity.WorkExpPhoto
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface WorkExpPhotoService {
    fun addPhotoOrVideo(photoOrVideo: List<MultipartFile>, idWorkExp:MstWorkExperience): ResponseEntity<BaseResponse<Any>>
    fun getPhotoOrVideoByProductId(idWorkExp:MstWorkExperience):ResponseEntity<BaseResponse<List<WorkExpPhotoResponse>>>
    fun loadPhoto(fileName:String):ResponseEntity<Any>
    fun deletePhoto(idWorkExpPhoto: Int): DeleteResponse
    fun checkPhotoExist(workExpList: List<WorkExpPhoto>): DeletePhotoDto
}