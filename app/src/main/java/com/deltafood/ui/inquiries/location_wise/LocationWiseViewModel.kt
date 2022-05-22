package com.deltafood.ui.inquiries.location_wise

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
class LocationWiseViewModel(
    private val repository: InquiryRepositories
) : ViewModel() {
    val appContext by lazy {
        repository.appContext
    }

    var alLocations :  MutableList<Locations> = mutableListOf()
    var locationsList = MutableLiveData<MutableList<Locations>>()
    val liveLocations : LiveData<MutableList<Locations>>
        get() = locationsList

    var productId : String?=null
    var alSearchedLocations :  MutableList<SearchedLocations> = mutableListOf()
    var searchedList = MutableLiveData<MutableList<SearchedLocations>>()
    val liveSearchedList : LiveData<MutableList<SearchedLocations>>
        get() = searchedList

    var listener: NetworkListener? = null
    var errorMessage: String = ""
    var filteredLocations :Locations?=null

    fun getLocations(site:String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getLocations(
                    XMLLocationListRequest(site)
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
                        locationsList?.postValue(alLocations)
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
                    if (child.attributes.item(0).nodeValue.equals("LOC"))
                        locations.locationName = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("WRH"))
                        locations.wareHouse = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("BLOCK"))
                        locations.blocked = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("CNTPROG"))
                        locations.countInProgress = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOCTYP"))
                        locations.locType = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOCCAT"))
                        locations.locCategory = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("STKLOC"))
                        locations.storageLocation = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("SPRODUCT"))
                        locations.singleProduct = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("DEDIDCATED"))
                        locations.dedicate = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("REPLENISH"))
                        locations.replenish = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("CAPMAN"))
                        locations.capMan = fields.nodeValue
                }
            }
            alLocations.add(locations)
        }

    }

    fun searchLocations(site :String,productId:String) {
        this.productId = productId
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.searchLocations(
                    XMLLocationSearchRequest(site,productId)
                )
                alSearchedLocations.clear()
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
                                    getOrdersNodeValue("LIN", lin)

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
    private fun getOrdersNodeValue(tag: String?, element: Element) {
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
                }
            }
            alSearchedLocations.add(products)
        }

    }

    fun filterLocations(locations:Locations,isFilterCleared : Boolean) {
        filteredLocations = locations
        var alLocationsNew : MutableList<Locations> = mutableListOf()
        if (isFilterCleared){
            alLocationsNew = alLocations
        }else{
            if (alLocations!=null){
                alLocations?.forEach {

                    if ((locations.locationName.isNotEmpty() && it?.locationName?.equals(locations.locationName,ignoreCase = true)) ||
                        (locations.wareHouse.isNotEmpty() &&  it?.wareHouse?.equals(locations.wareHouse,ignoreCase = true)) ||
                        (locations.locType.isNotEmpty() && it?.locType?.equals(locations.locType,ignoreCase = true)) ||
                        (locations.storageLocation.isNotEmpty() && it?.storageLocation?.equals(locations.storageLocation,ignoreCase = true))){
                        alLocationsNew.add(it)
                    }
                }
            }
        }


        locationsList?.postValue(alLocationsNew)
    }
}