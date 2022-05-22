package com.deltafood.ui.inquiries.product_site_stock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deltafood.R
import com.deltafood.data.model.request.XMLOrderProductRequest
import com.deltafood.data.model.request.XMLOrderSearchRequest
import com.deltafood.data.model.request.XMLProductRequest
import com.deltafood.data.model.request.XMLProductSiteSearchRequest
import com.deltafood.data.model.response.LoginApi
import com.deltafood.data.model.response.Orders
import com.deltafood.data.model.response.Products
import com.deltafood.data.model.response.SearchedLocations
import com.deltafood.data.repositories.InquiryRepositories
import com.deltafood.data.repositories.UserRepositories
import com.deltafood.utils.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import java.lang.StringBuilder
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by Noushad N on 02-05-2022.
 */
class ProductSiteStockViewModel(
    private val repository: InquiryRepositories
) : ViewModel() {
    val appContext by lazy {
        repository.appContext
    }

    var alProducts: MutableList<Products> = mutableListOf()
    var productsList = MutableLiveData<MutableList<Products>>()
    val liveProducts: LiveData<MutableList<Products>>
        get() = productsList

    var productId: String? = null
    var alOrders: MutableList<Orders> = mutableListOf()
    var ordersList = MutableLiveData<MutableList<Orders>>()
    val liveOrders: LiveData<MutableList<Orders>>
        get() = ordersList

    var alSearchedLocations :  MutableList<SearchedLocations> = mutableListOf()
    var searchedList = MutableLiveData<MutableList<SearchedLocations>>()
    val liveSearchedList : LiveData<MutableList<SearchedLocations>>
        get() = searchedList

    var listener: NetworkListener? = null
    var errorMessage: String = ""
    var filteredProducts: Products? = null

    fun getProducts(site: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getProducts(
                    XMLOrderProductRequest(site)
                )
                if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                    Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory =
                        DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document =
                        docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)))
                    val nList: NodeList? = doc.getElementsByTagName("TAB")
                    if (nList != null) {

                        // Iterating through this list
                        for (i in 0 until nList.length) {
                            if (nList.item(0).nodeType == Node.ELEMENT_NODE) {
                                val user: HashMap<String, String?> = HashMap()
                                val elm: Element = nList.item(i) as Element
                                val childNodes = elm?.childNodes
                                for (i in 0 until childNodes.length) {
                                    val lin: Element = childNodes.item(i) as Element
                                    getNodeValue("LIN", lin)

                                }
                            }
                        }
                        productsList?.postValue(alProducts)
                        listener?.onSuccess()
                    }
                }


            } catch (error: ErrorBodyException) {
                try {
                    println(error.message.toString())
                    listener?.onFailure()
                } catch (e: Exception) {
                    errorMessage = appContext.getString(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getString(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    // A function to get the node value while parsing
    private fun getNodeValue(tag: String?, element: Element) {
        var products = Products()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("PRODDESC"))
                        products.productName = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("PRODUCT"))
                        products.productId = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("REMQTY"))
                        products.remainingQTY = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("EXPRCPDT"))
                        products.expectedDate = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("POHNUM"))
                        products.orderNo = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("PLINE"))
                        products.orderLine = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("BP"))
                        products.supplierId = fields.nodeValue
                }
            }
            alProducts.add(products)
        }

    }

    fun searchOrders(site: String, productId: String, date: String) {
        this.productId = productId
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.searchOrders(
                    XMLOrderSearchRequest(site, productId, date)
                )
                alOrders.clear()
                if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                    Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory =
                        DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document =
                        docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)))
                    val nList: NodeList? = doc.getElementsByTagName("TAB")
                    if (nList != null) {

                        // Iterating through this list
                        for (i in 0 until nList.length) {
                            if (nList.item(0).nodeType == Node.ELEMENT_NODE) {
                                val user: HashMap<String, String?> = HashMap()
                                val elm: Element = nList.item(i) as Element
                                val childNodes = elm?.childNodes
                                for (i in 0 until childNodes.length) {
                                    val lin: Element = childNodes.item(i) as Element
                                    getOrdersNodeValue("LIN", lin)

                                }
                            }
                        }
                        ordersList?.postValue(alOrders)
                        listener?.onSuccess()
                    }
                }


            } catch (error: ErrorBodyException) {
                try {
                    println(error.message.toString())
                    listener?.onFailure()
                } catch (e: Exception) {
                    errorMessage = appContext.getString(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getString(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    // A function to get the node value while parsing
    private fun getOrdersNodeValue(tag: String?, element: Element) {
        var products = Orders()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("REMQTY"))
                        products.remainingQTY = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("EXPRCPDT"))
                        products.expectedDate = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("POHNUM"))
                        products.poNo = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("PLINE"))
                        products.poLine = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("BP"))
                        products.bp = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("POQTY"))
                        products.poQTY = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("UOM"))
                        products.uom = fields.nodeValue
                    products.productId = productId!!
                }
            }
            alOrders.add(products)
        }

    }

    fun filterProducts(products: Products, isFilterCleared: Boolean) {
        filteredProducts = products
        var alProductsNew: MutableList<Products> = mutableListOf()
        if (isFilterCleared) {
            alProductsNew = alProducts
        } else {
            if (alProducts != null) {
                alProducts?.forEach {
                    var orderLine = it?.orderLine?.filter { !it.isWhitespace() }
                    if (it?.productName?.equals(products?.productName, ignoreCase = true) ||
                        getDate(it?.expectedDate)?.before(getDate(products?.expectedDate))!! ||
//                        getDate(it?.expectedDate)?.equals(getDate(products.expectedDate))!! ||
                        (products.orderNo.isNotEmpty() && it?.orderNo?.equals(products.orderNo, ignoreCase = true)) ||
                        (products.orderLine.isNotEmpty() && orderLine?.equals(products.orderLine, ignoreCase = true)) ||
                        (products.supplierId.isNotEmpty() && it?.supplierId?.equals(products.supplierId, ignoreCase = true)) ||
                        (products.remainingQTY.isNotEmpty() && it?.remainingQTY?.equals(products.remainingQTY, ignoreCase = true))
                    ) {
                        alProductsNew.add(it)
                    }
                }
            }
        }


        productsList?.postValue(alProductsNew)
    }


    fun getProductsBySite(site: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getProductsBySite(
                    XMLProductRequest(site)
                )
                if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                    Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory =
                        DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document =
                        docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)))
                    val nList: NodeList? = doc.getElementsByTagName("TAB")
                    if (nList != null) {

                        // Iterating through this list
                        for (i in 0 until nList.length) {
                            if (nList.item(0).nodeType == Node.ELEMENT_NODE) {
                                val user: HashMap<String, String?> = HashMap()
                                val elm: Element = nList.item(i) as Element
                                val childNodes = elm?.childNodes
                                for (i in 0 until childNodes.length) {
                                    val lin: Element = childNodes.item(i) as Element
                                    getNodeValueForProductSite("LIN", lin)

                                }
                            }
                        }
                        productsList?.postValue(alProducts)
                        listener?.onSuccess()
                    }
                }


            } catch (error: ErrorBodyException) {
                try {
                    println(error.message.toString())
                    listener?.onFailure()
                } catch (e: Exception) {
                    errorMessage = appContext.getString(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getString(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    // A function to get the node value while parsing
    private fun getNodeValueForProductSite(tag: String?, element: Element) {
        var products = Products()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("PRODUCTDESC"))
                        products.productName = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("PRODUCT"))
                        products.productId = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("CATG"))
                        products.category = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("STA"))
                        products.status = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("STOCK"))
                        products.stockManagement = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOT"))
                        products.locManagement = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("SER"))
                        products.serialNo = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("BUY"))
                        products.buyer = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("CNTMODE"))
                        products.countMode = fields.nodeValue

                }
            }
            alProducts.add(products)
        }

    }

    fun filterProductsForSites(products: Products, isFilterCleared: Boolean) {
        filteredProducts = products
        var alProductsNew: MutableList<Products> = mutableListOf()
        if (isFilterCleared) {
            alProductsNew = alProducts
        } else {
            if (alProducts != null) {
                alProducts?.forEach {
                    if (it?.productId?.equals(products.productName, ignoreCase = true) ||
                        it?.category?.equals(products.category, ignoreCase = true) ||
                        (products.buyer.isNotEmpty() && it?.buyer?.equals(products.buyer, ignoreCase = true)) ||
                        (products.stockManagement.isNotEmpty() && it?.stockManagement.equals(products.stockManagement, ignoreCase = true)) ||
                        (products.locManagement.isNotEmpty() && it?.locManagement?.equals(products.locManagement, ignoreCase = true)) ||
                        (products.serialNo.isNotEmpty() && it?.serialNo?.equals(products.serialNo, ignoreCase = true)) ||
                        (products.status.isNotEmpty() && it?.status?.equals(products.status, ignoreCase = true))
                    ) {
                        alProductsNew.add(it)
                    }
                }
            }
        }


        productsList?.postValue(alProductsNew)
    }

    fun searchProducts(site: String, productId: String) {
        this.productId = productId
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.searchProductsBySite(
                    XMLProductSiteSearchRequest(site, productId)
                )
                alSearchedLocations.clear()
                if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                    Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory =
                        DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document =
                        docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)))
                    val nList: NodeList? = doc.getElementsByTagName("TAB")
                    if (nList != null) {

                        // Iterating through this list
                        for (i in 0 until nList.length) {
                            if (nList.item(0).nodeType == Node.ELEMENT_NODE) {
                                val user: HashMap<String, String?> = HashMap()
                                val elm: Element = nList.item(i) as Element
                                val childNodes = elm?.childNodes
                                for (i in 0 until childNodes.length) {
                                    val lin: Element = childNodes.item(i) as Element
                                    getProductsNodeValue("LIN", lin)

                                }
                            }
                        }
                        searchedList?.postValue(alSearchedLocations)
                        listener?.onSuccess()
                    }
                }


            } catch (error: ErrorBodyException) {
                try {
                    println(error.message.toString())
                    listener?.onFailure()
                } catch (e: Exception) {
                    errorMessage = appContext.getString(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getString(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    // A function to get the node value while parsing
    private fun getProductsNodeValue(tag: String?, element: Element) {
        var products = SearchedLocations()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length){
                val child : Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("PRODUCT"))
                        products.productId = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOT"))
                        products.locationName = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("SLOT"))
                        products.subLoc = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("SERIAL"))
                        products.serialNo = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("PACQTY"))
                        products.pacQuantity = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("PACUOM"))
                        products.packingUnit = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("STKQTY"))
                        products.stkQuantity = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("ALLOCATED"))
                        products.allowedQuantity = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("OWNER"))
                        products.owner = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("STA"))
                        products.status = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOC"))
                        products.location = fields.nodeValue
                    products?.productId = productId!!
                }
            }
            alSearchedLocations.add(products)
        }

    }
}