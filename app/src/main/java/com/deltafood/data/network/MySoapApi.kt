package com.deltafood.data.network

import ProductResponse
import XMLCreateResponse
import XMLResponse
import XMLStockCreateResponse
import com.deltafood.data.model.request.*
import com.deltafood.utils.BASE_SOAP_API_URL
import okhttp3.OkHttpClient
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.strategy.Strategy
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


/*
 *Created by Noushad on 02-05-2022
*/

interface MySoapApi {

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getProducts(
        @Body body: XMLOrderProductRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun searchProducts(
        @Body body: XMLOrderSearchRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getLocations(
        @Body body: XMLLocationListRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun searchLocations(
        @Body body: XMLLocationSearchRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getProductsBySite(
        @Body body: XMLProductRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun searchProductsBySite(
        @Body body: XMLProductSiteSearchRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getPurchaseOrderList(
        @Body body: XMLPurchaseOrderListRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun searchPOInquiries(
        @Body body: XMLSearchPoInquiriesListRequest
    ) : Response<ProductResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getTransactions(
        @Body body: XMLTransactionTypeRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getUnits(
        @Body body: XMLUnitProuctsRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getStatus(
        @Body body: XMLStatusRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun createPurchaseReceipt(
        @Body body: XMLCreatePurchaseReceiptRequest
    ) : Response<XMLCreateResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun createMiscReceipt(
        @Body body: XMLCreateMiscReceiptRequest
    ) : Response<XMLCreateResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getTransaction(
        @Body body: XMLTransactionRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getStockLocations(
        @Body body: XMLStockLocationsRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getStock(
        @Body body: XMLStockListRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getSites(
        @Body body: XMLSitesRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun checkProductManaged(
        @Body body: XMLCheckProductSiteRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getProductQuantity(
        @Body body: XMLProductQuantityRequest
    ) : Response<ProductResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun getStockByLot(
        @Body body: XMLLotRequest
    ) : Response<XMLResponse>

    @Headers(
        "Content-Type: text/xml;charset=UTF-8",
        "soapaction: read"
    )
    @POST("CAdxWebServiceXmlCC?wsdl/")
    suspend fun createStockChange(
        @Body body:  XMLCreateStockChangeRequest
    ) : Response<XMLStockCreateResponse>

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MySoapApi {
            val logging = CustomInterceptor()
            logging.level = CustomInterceptor.Level.BODY
            val strategy: Strategy = AnnotationStrategy()

            val serializer: Serializer = Persister(strategy)
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .callTimeout(300, TimeUnit.SECONDS)
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BASE_SOAP_API_URL)
//                .baseUrl(TEST_BASE_API_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .build()
                .create(MySoapApi::class.java)
        }
    }

}