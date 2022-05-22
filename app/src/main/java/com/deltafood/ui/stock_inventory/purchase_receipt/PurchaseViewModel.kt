package com.deltafood.ui.stock_inventory.purchase_receipt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deltafood.R
import com.deltafood.data.model.request.*
import com.deltafood.data.model.response.*
import com.deltafood.data.repositories.InquiryRepositories
import com.deltafood.data.repositories.PurchaseRepositories
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
class PurchaseViewModel(
    private val repository: PurchaseRepositories
) : ViewModel() {
    val appContext by lazy {
        repository.appContext
    }

    var alUnits :  ArrayList<com.deltafood.data.model.response.Unit> = arrayListOf()
    var unitList = MutableLiveData<MutableList<com.deltafood.data.model.response.Unit>>()
    val liveUnits : LiveData<MutableList<com.deltafood.data.model.response.Unit>>
        get() = unitList

    var alStatus :  ArrayList<Status> = arrayListOf()
    var statusList = MutableLiveData<MutableList<Status>>()
    val liveStatus : LiveData<MutableList<Status>>
        get() = statusList

    var transactions = ""
    var transaction = MutableLiveData<String>()
    var listener: NetworkListener? = null
    var errorMessage: String = ""
    var units = MutableLiveData<String>()
    var filteredStatus :Status?=null

    fun getTransactions() {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getTransactions(
                    XMLTransactionTypeRequest()
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
                        transaction?.postValue(transactions)
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
        var locations = Locations()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length){
                val child : Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("CODE"))
                        transactions = fields.nodeValue
                }
            }

        }

    }

    fun getUnits(productId:String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getUnits(
                    XMLUnitProuctsRequest(productId)
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
                                    getUnitsNodeValue("LIN", lin)

                                }
                            }
                        }
                        unitList?.postValue(alUnits)
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
    private fun getUnitsNodeValue(tag: String?, element: Element) {
        var unit = Unit()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length){
                val child : Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("UOM"))
                        unit.uom = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("UOMCON"))
                        unit.uomCon = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("UOMDESC"))
                        unit.description = fields.nodeValue
                }
            }

            alUnits.add(unit)
        }

    }


    fun getStatus(site:String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getStatus(
                    XMLStatusRequest(site)
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
                                    getStatusNodeValue("LIN", lin)

                                }
                            }
                        }
                        statusList?.postValue(alStatus)
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
    private fun getStatusNodeValue(tag: String?, element: Element) {
        var status = Status()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length){
                val child : Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("STA"))
                        status.status = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("STASDES"))
                        status.description = fields.nodeValue
                }
            }

            alStatus.add(status)
        }

    }

    fun filterStatus(status:Status,isFilterCleared : Boolean) {
        filteredStatus = status
        var alStatusNew : MutableList<Status> = mutableListOf()
        if (isFilterCleared){
            alStatusNew = alStatus
        }else{
            if (alStatus!=null){
                alStatus?.forEach {

                    if ((status.status.isNotEmpty() && it?.status?.equals(status.status,ignoreCase = true)) ||
                        (status.description.isNotEmpty() &&  it?.description?.equals(status.description,ignoreCase = true))){
                        alStatusNew.add(it)
                    }
                }
            }
        }


        statusList?.postValue(alStatusNew)
    }
}