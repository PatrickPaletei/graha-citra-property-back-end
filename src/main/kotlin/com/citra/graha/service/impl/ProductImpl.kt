package com.citra.graha.service.impl

import com.citra.graha.dto.request.AddProductRequest
import com.citra.graha.dto.request.UpdateProductRequest
import com.citra.graha.dto.response.BaseResponse
import com.citra.graha.entity.MstProduct
import com.citra.graha.entity.MstPropertyType
import com.citra.graha.entity.MstStatus
import com.citra.graha.repository.ProductRepository
import com.citra.graha.repository.PropertyTypeRepository
import com.citra.graha.repository.StatusRepository
import com.citra.graha.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ProductImpl(
    val productRepository: ProductRepository,
    val propertyTypeRepository: PropertyTypeRepository,
    val statusRepository: StatusRepository
): ProductService {
    override fun addProduct(product: AddProductRequest, propertyType: MstPropertyType, status: MstStatus): ResponseEntity<BaseResponse<MstProduct>> {
        val mstProduct = MstProduct(
            productDescription = product.productDescription,
            productElectricity = product.productElectricity,
            productAddress = product.productAddress,
            productBathroom = product.productBathroom,
            productLuasBangunan = product.productLuasBangunan,
            productVisitCount = product.productVisitCount,
            productLuasTanah = product.productLuasTanah,
            propertyId = propertyType,
            productBedroom = product.productBedroom,
            statusId = status,
            dtAdded = Instant.now(),
            dtUpdated = Instant.now(),
            dtSold = null
        )
        productRepository.save(mstProduct)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            BaseResponse(
                status = "T",
                message = "Add Product Successfully"
            )
        )
    }

    override fun deleteProduct(product: MstProduct): ResponseEntity<BaseResponse<Any>> {
        productRepository.delete(product)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Delete Product Successfully"
            )
        )
    }

    override fun getAll(): ResponseEntity<BaseResponse<List<MstProduct>>> {
        val allProducts = productRepository.findAll()
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get all Products",
                data = allProducts
            )
        )
    }

    override fun getById(id: Int): ResponseEntity<BaseResponse<MstProduct>> {
        val existProduct = productRepository.findById(id)
        if (existProduct.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product ${id} not found"
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get product by id",
                data = existProduct.get()
            )
        )
    }

    override fun getByPropertyTypeName(propertyType: String): ResponseEntity<BaseResponse<List<MstProduct>>> {
        val existProperty = propertyTypeRepository.findByPropertyNameIgnoreCase(propertyType)
        if(existProperty.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "$propertyType not found"
                )
            )
        }
        val existProduct = productRepository.findByPropertyId(existProperty.get())
        if (existProduct.isEmpty()){
            return ResponseEntity.ok(
                BaseResponse(
                    status = "T",
                    message = "No products found with property type $propertyType",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get by property type $propertyType",
                data = existProduct
            )
        )
    }

    override fun getByPropertyTypeId(propertyType: Int): ResponseEntity<BaseResponse<List<MstProduct>>> {
        val existProperty = propertyTypeRepository.findById(propertyType)
        if(existProperty.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "$propertyType not found"
                )
            )
        }
        val existProduct = productRepository.findByPropertyId(existProperty.get())
        if (existProduct.isEmpty()){
            return ResponseEntity.ok(
                BaseResponse(
                    status = "T",
                    message = "No products found with property type $propertyType",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get by property type $propertyType",
                data = existProduct
            )
        )
    }

    override fun getByStatusName(statusName: String): ResponseEntity<BaseResponse<List<MstProduct>>> {
        val existStatus = statusRepository.findByStatusNameIgnoreCase(statusName)
        if(existStatus.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "$statusName not found"
                )
            )
        }
        val existProduct = productRepository.findByStatusId(existStatus.get())
        if (existProduct.isEmpty()){
            return ResponseEntity.ok(
                BaseResponse(
                    status = "T",
                    message = "No products found with status name $statusName",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get by status name $statusName",
                data = existProduct
            )
        )
    }

    override fun getByStatusId(statusId: Int): ResponseEntity<BaseResponse<List<MstProduct>>> {
        val existStatus = statusRepository.findById(statusId)
        if(existStatus.isEmpty){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "$statusId not found"
                )
            )
        }
        val existProduct = productRepository.findByStatusId(existStatus.get())
        if (existProduct.isEmpty()){
            return ResponseEntity.ok(
                BaseResponse(
                    status = "T",
                    message = "No products found with status id $statusId",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Get by status id $statusId",
                data = existProduct
            )
        )
    }

    override fun updateProduct(
        existProduct: MstProduct,
        product: UpdateProductRequest,
        propertyType: MstPropertyType?,
        status: MstStatus?
    ): ResponseEntity<BaseResponse<MstProduct>> {
        // what ID shows that the product has been sold?
        // let's say SOLD = 1
        var dtSold: Instant? = null
        if (status != null && status.statusId == 1){
            dtSold = Instant.now()
        }

        val updatedProduct = existProduct.copy(
            productAddress = product.productAddress ?: existProduct.productAddress,
            productBedroom = product.productBedroom ?: existProduct.productBedroom,
            propertyId = propertyType ?: existProduct.propertyId,
            productDescription = product.productDescription ?: existProduct.productDescription,
            productBathroom = product.productBathroom ?: existProduct.productBathroom,
            productLuasTanah = product.productLuasTanah ?: existProduct.productLuasTanah,
            productLuasBangunan = product.productLuasBangunan ?: existProduct.productLuasBangunan,
            productElectricity = product.productElectricity ?: existProduct.productElectricity,
            statusId = status ?: existProduct.statusId,
            productVisitCount = product.productVisitCount ?: existProduct.productVisitCount,
            dtUpdated = Instant.now(),
            dtSold = dtSold ?: existProduct.dtSold
        )
        productRepository.save(updatedProduct)
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Product updated",
                data = updatedProduct
            )
        )
    }

    override fun searchProduct(searchValue: String): ResponseEntity<BaseResponse<List<MstProduct>>> {
        val productSearch = productRepository.searchProduct(searchValue)
        if(productSearch.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse(
                    status = "F",
                    message = "Product not found",
                    data = null
                )
            )
        }
        return ResponseEntity.ok(
            BaseResponse(
                status = "T",
                message = "Search product",
                data = productSearch
            )
        )
    }
}