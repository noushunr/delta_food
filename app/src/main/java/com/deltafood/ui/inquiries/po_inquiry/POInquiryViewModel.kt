package com.deltafood.ui.inquiries.po_inquiry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deltafood.R
import com.deltafood.data.model.request.*
import com.deltafood.data.model.response.*
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
class POInquiryViewModel(
    private val repository: InquiryRepositories
) : ViewModel() {
    val appContext by lazy {
        repository.appContext
    }

    var pohNo = ""
    var productId = ""
    var date = ""

    var alPurchasedOrders :  MutableList<PurchaseOrder> = mutableListOf()
    var ordersList = MutableLiveData<MutableList<PurchaseOrder>>()
    val liveOrders : LiveData<MutableList<PurchaseOrder>>
        get() = ordersList

    var alPOInquiry :  MutableList<POInquiry> = mutableListOf()
    var poInquiryList = MutableLiveData<MutableList<POInquiry>>()
    val livePOInquiries : LiveData<MutableList<POInquiry>>
        get() = poInquiryList

    var listener: NetworkListener? = null
    var errorMessage: String = ""
    var filteredOrders :PurchaseOrder?=null
    var filteredPoInquiry :POInquiry?=null
    fun getPurchaseOrderList(site:String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getPurchaseOrderList(
                    XMLPurchaseOrderListRequest(site)
                )
                if (response?.rootElement2?.query?.queryReturn?.resultXml!=null){
                    Log.d("Messages",response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)) )
                    val nList: NodeList? = doc.getElementsByTagName("TAB")
                    if (nList!=null){

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
                        ordersList?.postValue(alPurchasedOrders)
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
        var purchaseOrder = PurchaseOrder()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length){
                val child : Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.hasChildNodes()) {
                        var fields = child.firstChild
                        if (child.attributes.item(0).nodeValue.equals("POHNUM"))
                            purchaseOrder.poNo = fields.nodeValue
                        if (child.attributes.item(0).nodeValue.equals("EXPDATE"))
                            purchaseOrder.expectedDate = fields.nodeValue
                        if (child.attributes.item(0).nodeValue.equals("POHFCY"))
                            purchaseOrder.siteId = fields.nodeValue
                        if (child.attributes.item(0).nodeValue.equals("BPS"))
                            purchaseOrder.vendorId = fields.nodeValue
                        if (child.attributes.item(0).nodeValue.equals("REF"))
                            purchaseOrder.ref = fields.nodeValue

                    }
                }
            }
            alPurchasedOrders.add(purchaseOrder)
        }

    }


    fun filterOrders(purchaseOrder: PurchaseOrder,isFilterCleared : Boolean) {
        filteredOrders = purchaseOrder
        var alOrdersNew : MutableList<PurchaseOrder> = mutableListOf()
        if (isFilterCleared){
            alOrdersNew = alPurchasedOrders
        }else{
            if (alPurchasedOrders!=null){
                alPurchasedOrders?.forEach {

                    if (it?.poNo?.equals(purchaseOrder.poNo,ignoreCase = true) ||
                        it?.siteId?.equals(purchaseOrder.siteId,ignoreCase = true) ||
                        getDate(it?.expectedDate)?.before(getDate(purchaseOrder.expectedDate))!! ||
//                        getDate(it?.expectedDate)?.equals(getDate(purchaseOrder.expectedDate))!! ||
                        it?.vendorId?.equals(purchaseOrder.vendorId,ignoreCase = true) ||
                        (purchaseOrder.ref.isNotEmpty() && it?.ref?.equals(purchaseOrder.ref,ignoreCase = true))){
                        alOrdersNew.add(it)
                    }
                }
            }
        }


        ordersList?.postValue(alOrdersNew)
    }

    fun searchPOInquiries(pohNo:String, productNo: String,date:String ){
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()
        CoRoutines.main {
            try {
                alPOInquiry.clear()
                this.pohNo = pohNo
                productId = productNo
                this.date = date
                val response = repository.searchPOInquiries(
                    XMLSearchPoInquiriesListRequest(pohNo)
                )
                if (response?.rootElement2?.query?.queryReturn?.resultXml!=null){
                    Log.d("Messages",response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)) )
                    val nList: NodeList? = doc.getElementsByTagName("TAB")
                    if (nList!=null){

                        // Iterating through this list
                        for (i in 0 until nList.length) {
                            if (nList.item(0).nodeType == Node.ELEMENT_NODE) {
                                val user: HashMap<String, String?> = HashMap()
                                val elm: Element = nList.item(i) as Element
                                val childNodes = elm?.childNodes
                                for (i in 0 until childNodes.length) {
                                    val lin: Element = childNodes.item(i) as Element
                                    getPOInquiryNodeValue("LIN", lin)

                                }
                            }
                        }

                    }
                }
                poInquiryList?.postValue(alPOInquiry)
                listener?.onSuccess()
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
    private fun getPOInquiryNodeValue(tag: String?, element: Element) {
        var poInquiry = POInquiry()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length){
                val child : Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.hasChildNodes()) {
                        var fields = child.firstChild
                        poInquiry.poNo = pohNo

                        var item = child.attributes.getNamedItem("NAME")
                        if (item!=null){
                            if (item.nodeValue.equals("POHNUM"))
                                poInquiry.poNo = fields.nodeValue
                            if (item.nodeValue.equals("EXTRCPDAT"))
                                poInquiry.expectedDate = fields.nodeValue
                            if (item.nodeValue.equals("POPLIN"))
                                poInquiry.lineNo = fields.nodeValue
                            if (item.nodeValue.equals("ITMREF"))
                                poInquiry.productId = fields.nodeValue
                            if (item.nodeValue.equals("ITMDES"))
                                poInquiry.description = fields.nodeValue
                            if (item.nodeValue.equals("STU"))
                                poInquiry.uomStu = fields.nodeValue
                            if (item.nodeValue.equals("UOM"))
                                poInquiry.uom = fields.nodeValue
                            if (item.nodeValue.equals("QTYSTU"))
                                poInquiry.remQty = fields.nodeValue
                            if (item.nodeValue.equals("LINCLEFLG"))
                                poInquiry.type = fields.nodeValue
                            if (item.nodeValue.equals("QTYUOM"))
                                poInquiry.qtyuom = fields.nodeValue
                            if (item.nodeValue.equals("RCPQTYSTU"))
                                poInquiry.rcpQty = fields.nodeValue
                        }
                    }
                }
            }
            if (poInquiry.type != "2"){
                if (productId.isNotEmpty()){
                    if (productId == poInquiry.productId){
                        if (date?.isNotEmpty() && getDate(poInquiry.expectedDate)?.before(getDate(date))!!)
                            alPOInquiry.add(poInquiry)
                    }
                }else{
                    if (date?.isNotEmpty()){
                      if (getDate(poInquiry.expectedDate)?.before(getDate(date))!!)
                          alPOInquiry.add(poInquiry)
                    }else{
                        alPOInquiry.add(poInquiry)
                    }

                }

            }

        }

    }

    fun filterPOInquiries(poInquiry: POInquiry,isFilterCleared : Boolean) {
        filteredPoInquiry = poInquiry
        var alInquiriesNew : MutableList<POInquiry> = mutableListOf()
        if (isFilterCleared){
            alInquiriesNew = alPOInquiry
        }else{
            if (alPOInquiry!=null){
                alPOInquiry?.forEach {

                    if ((poInquiry.poNo.isNotEmpty() && it?.poNo?.equals(poInquiry.poNo,ignoreCase = true)) ||
                        it?.productId?.equals(poInquiry.productId,ignoreCase = true) ||
                        getDate(it?.expectedDate)?.before(getDate(poInquiry.expectedDate))!! ||
//                        getDate(it?.expectedDate)?.equals(getDate(purchaseOrder.expectedDate))!! ||
                        it?.uom?.equals(poInquiry.uom,ignoreCase = true) ||
                        it?.lineNo?.equals(poInquiry.lineNo,ignoreCase = true) ||
                        it?.remQty?.equals(poInquiry.remQty,ignoreCase = true)){
                        alInquiriesNew.add(it)
                    }
                }
            }
        }


        poInquiryList?.postValue(alInquiriesNew)
    }
}