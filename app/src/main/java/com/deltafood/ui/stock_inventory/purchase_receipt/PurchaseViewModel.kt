package com.deltafood.ui.stock_inventory.purchase_receipt

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deltafood.R
import com.deltafood.data.model.request.*
import com.deltafood.data.model.response.*
import com.deltafood.data.repositories.PurchaseRepositories
import com.deltafood.database.entities.InterSiteStock
import com.deltafood.database.entities.MiscProducts
import com.deltafood.database.entities.Products
import com.deltafood.database.entities.StockChange
import com.deltafood.utils.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
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

    var alUnits: ArrayList<com.deltafood.data.model.response.Unit> = arrayListOf()
    var unitList = MutableLiveData<MutableList<com.deltafood.data.model.response.Unit>>()
    val liveUnits: LiveData<MutableList<com.deltafood.data.model.response.Unit>>
        get() = unitList

    var alStockLocations: ArrayList<StockLocations> = arrayListOf()
    var stockLocationsList = MutableLiveData<MutableList<StockLocations>>()

    var alStatus: ArrayList<Status> = arrayListOf()
    var statusList = MutableLiveData<MutableList<Status>>()
    val liveStatus: LiveData<MutableList<Status>>
        get() = statusList

    var alTransactions: ArrayList<Transactions> = arrayListOf()
    var transactionList = MutableLiveData<MutableList<Transactions>>()
    var transactions = ""
    var transaction = MutableLiveData<String>()
    var listener: NetworkListener? = null
    var errorMessage: String = ""
    var units = MutableLiveData<String>()
    var unit = MutableLiveData<String>()
    var unitCom = MutableLiveData<String>()
    var filteredStatus: Status? = null
    var filteredTransactions: Transactions? = null
    var successMessage = MutableLiveData<String>()
    var stockLocations = MutableLiveData<String>()
    var loc = MutableLiveData<String>()
    var alStocks: ArrayList<Stock> = arrayListOf()
    var stocksList = MutableLiveData<MutableList<Stock>>()

    var alSites: ArrayList<Sites> = arrayListOf()
    var sitesList = MutableLiveData<MutableList<Sites>>()
    var filteredSites: Sites? = null
    var isProductManaged = MutableLiveData<Boolean>()
    var productQuantity = MutableLiveData<ProductQuantity>()

    var alStockByLot: ArrayList<StockByLot> = arrayListOf()
    var stocksByLotList = MutableLiveData<MutableList<StockByLot>>()
    var filteredStockByLot: StockByLot? = null
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
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("CODE"))
                        transactions = fields.nodeValue
                }
            }

        }

    }

    fun getUnits(productId: String, functionName: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                alUnits?.clear()
                val response = repository.getUnits(
                    XMLUnitProuctsRequest(productId, functionName)
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
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
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


    fun getStatus(site: String, methodName: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getStatus(
                    XMLStatusRequest(site,methodName)
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
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
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

    fun filterStatus(status: Status, isFilterCleared: Boolean) {
        filteredStatus = status
        var alStatusNew: MutableList<Status> = mutableListOf()
        if (isFilterCleared) {
            alStatusNew = alStatus
        } else {
            if (alStatus != null) {
                alStatus?.forEach {

                    if ((status.status.isNotEmpty() && it?.status?.equals(
                            status.status,
                            ignoreCase = true
                        )) ||
                        (status.description.isNotEmpty() && it?.description?.equals(
                            status.description,
                            ignoreCase = true
                        ))
                    ) {
                        alStatusNew.add(it)
                    }
                }
            }
        }


        statusList?.postValue(alStatusNew)
    }


    fun filterTransactions(transactions: Transactions, isFilterCleared: Boolean) {
        filteredTransactions = transactions
        var alTransactionsNew: MutableList<Transactions> = mutableListOf()
        if (isFilterCleared) {
            alTransactionsNew = alTransactions
        } else {
            if (alTransactions != null) {
                alTransactions?.forEach {
                    if ((transactions.code?.isNotEmpty()!! && it?.code?.equals(
                            transactions.code,
                            ignoreCase = true
                        )!!)
                    ) {
                        alTransactionsNew.add(it)
                    }
                }
            }
        }


        transactionList?.postValue(alTransactionsNew)
    }


    fun filterSites(site: Sites, isFilterCleared: Boolean) {
        filteredSites = site
        var alSitesNew: MutableList<Sites> = mutableListOf()
        if (isFilterCleared) {
            alSitesNew = alSites
        } else {
            if (alSites != null) {
                alSites?.forEach {
                    if ((site.companyName?.isNotEmpty()!! && it?.companyName?.equals(
                            site.companyName,
                            ignoreCase = true
                        )!!)
                        || (site.site?.isNotEmpty()!! && it?.site?.equals(
                            site.site,
                            ignoreCase = true
                        )!!)
                        || (site.name?.isNotEmpty()!! && it?.name?.equals(
                            site.name,
                            ignoreCase = true
                        )!!)
                    ) {
                        alSitesNew.add(it)
                    }
                }
            }
        }


        sitesList?.postValue(alSitesNew)
    }

    fun saveProduct(products: Products) = repository?.saveProduct(products)

    fun getAllProducts() = repository?.getAllProducts()

    fun deleteAll() = repository?.deleteAllProducts()

    fun getProducts() = repository?.getProducts()

    fun saveMiscProduct(products: MiscProducts) = repository?.saveMiscProduct(products)

    fun getAllMiscProducts() = repository?.getAllMiscProducts()

    fun deleteMiscAll() = repository?.deleteAllMiscProducts()

    fun getMiscProducts() = repository?.getMiscProducts()

    fun saveStockProduct(products: StockChange) = repository?.saveStockProduct(products)

    fun deleteAllStock() = repository?.deleteAllStockProducts()

    fun getStockProducts() = repository?.getStockProducts()

    fun saveInterSite(products: InterSiteStock) = repository?.saveInterSite(products)

    fun deleteAllInterSite() = repository?.deleteAllInterSite()

    fun getInterSiteProducts() = repository?.getInterSiteProducts()

    fun createReceipt(inputXML: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.createPurchaseReceipt(
                    XMLCreatePurchaseReceiptRequest(inputXML)
                )
                if (response != null) {
                    if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                        Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                        // Steps to convert this input stream into a list
                        val builderFactory: DocumentBuilderFactory =
                            DocumentBuilderFactory.newInstance()
                        val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                        var status = response?.rootElement2?.query?.queryReturn?.status
                        if (status == 1) {
                            var message = ""
                            if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                                message =
                                    response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                            }
                            successMessage?.postValue("Purchase receipt created $message")

                            listener?.onSuccess()
                            deleteAll()
                        } else {
                            if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                                errorMessage =
                                    response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                                listener?.onFailure()
                            }
                        }
                    } else {
                        if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                            errorMessage =
                                response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                            listener?.onFailure()
                        }

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

    fun createMiscReceipt(inputXML: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.createMiscReceipt(
                    XMLCreateMiscReceiptRequest(inputXML)
                )
                if (response != null) {
                    if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                        Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                        // Steps to convert this input stream into a list
                        val builderFactory: DocumentBuilderFactory =
                            DocumentBuilderFactory.newInstance()
                        val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                        var status = response?.rootElement2?.query?.queryReturn?.status
                        if (status == 1) {
                            var message = ""
                            if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                                message =
                                    response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                            }
                            successMessage?.postValue("Miscellaneous receipt created $message")

                            listener?.onSuccess()
                            deleteMiscAll()
                        } else {
                            if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                                errorMessage =
                                    response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                                listener?.onFailure()
                            }
                        }
                    } else {
                        if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                            errorMessage =
                                response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                            listener?.onFailure()
                        }

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

    fun getTransaction(type: String, subType: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getTransaction(
                    XMLTransactionRequest(type, subType)
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
                                    getTransactionNodeValue("LIN", lin)

                                }
                            }
                        }
                        transactionList?.postValue(alTransactions)
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
    private fun getTransactionNodeValue(tag: String?, element: Element) {
        if (element.hasChildNodes()) {
            var transactions = Transactions()
            for (i in 0 until element.childNodes.length) {

                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("CODE"))
                        transactions.code = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("NAME"))
                        transactions.name = fields.nodeValue
                }
            }
            alTransactions?.add(transactions)

        }

    }

    fun getStockLocations(productId: String, site: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                alStockLocations?.clear()
                val response = repository.getStockLocations(
                    XMLStockLocationsRequest(productId, site)
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
                                    getStockLocationsNodeValue("LIN", lin)

                                }
                            }
                        }
                        stockLocationsList?.postValue(alStockLocations)
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
    private fun getStockLocationsNodeValue(tag: String?, element: Element) {
        var stockLocations = StockLocations()
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("LOC"))
                        stockLocations.location = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOCTYP"))
                        stockLocations.locType = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOCCAT"))
                        stockLocations.locCategory = fields.nodeValue
                }
            }

            alStockLocations.add(stockLocations)
        }

    }

    fun getStocks(site: String, productId: String, loc: String, status: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getStock(
                    XMLStockListRequest(site, productId, loc, status)
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
                                    getStockNodeValue("LIN", lin)

                                }
                            }
                        }
                        stocksList?.postValue(alStocks)
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
    private fun getStockNodeValue(tag: String?, element: Element) {
        if (element.hasChildNodes()) {
            var stock = Stock()
            for (i in 0 until element.childNodes.length) {

                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("SERIAL"))
                        stock.serial = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOT"))
                        stock.lot = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("SLOT"))
                        stock.slot = fields.nodeValue
                }
            }
            alStocks?.add(stock)

        }

    }

    fun getSites() {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getSites(
                    XMLSitesRequest()
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
                                    getSitesNodeValue("LIN", lin)

                                }
                            }
                        }
                        sitesList?.postValue(alSites)
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
    private fun getSitesNodeValue(tag: String?, element: Element) {
        if (element.hasChildNodes()) {
            var sites = Sites()
            for (i in 0 until element.childNodes.length) {

                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("FCYNAME"))
                        sites.name = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("CPY"))
                        sites.description = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("FCY"))
                        sites.site = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("CPYNAME"))
                        sites.companyName = fields.nodeValue
                }
            }
            alSites?.add(sites)

        }

    }


    fun isProductManaged(site: String, productId: String ) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.checkProductManaged(
                    XMLCheckProductSiteRequest(site,productId)
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
                                if (elm.hasChildNodes()){
                                    isProductManaged?.postValue(true)
                                }else{
                                    isProductManaged?.postValue(false)
                                }

                            }
                        }

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
    private fun getProductManagedNodeValue(tag: String?, element: Element) {
        if (element.hasChildNodes()) {
            for (i in 0 until element.childNodes.length) {

                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {

                }
            }


        }

    }

    fun getProductQuantity(productId: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getProductQuantity(
                    XMLProductQuantityRequest(productId)
                )
                if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                    Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                    // Steps to convert this input stream into a list
                    val builderFactory: DocumentBuilderFactory =
                        DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                    val doc: Document =
                        docBuilder.parse(InputSource(StringReader(response?.rootElement2?.query?.queryReturn?.resultXml!!)))
                    val nList: NodeList? = doc.childNodes
                    if (nList != null) {

                        // Iterating through this list
                        for (i in 0 until nList.length) {
                            if (nList.item(0).nodeType == Node.ELEMENT_NODE) {
                                val elm: Element = nList.item(i) as Element
                                val childNodes = elm?.childNodes
                                for (i in 0 until childNodes.length) {
                                    val grp: Element = childNodes.item(i) as Element
                                    getProductNodeValue(grp)

                                }
                            }
                        }
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
    private fun getProductNodeValue(element: Element) {
        if (element.hasChildNodes()) {
            var productQuantity = ProductQuantity()
            for (i in 0 until element.childNodes.length) {
                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("PUUSTUCOE")) {
                        productQuantity.puQuantity = fields.nodeValue.toDouble()
                    }
                    if (child.attributes.item(0).nodeValue.equals("PUU")) {
                        productQuantity.purchaseUnity = fields.nodeValue

                    }
                }
            }
            if (!productQuantity?.purchaseUnity.isNullOrEmpty()){
                this.productQuantity.postValue(productQuantity)
            }

        }

    }

    fun getStocksByLot(productId: String) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.getStockByLot(
                    XMLLotRequest(productId,"ZITMSER")
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
                                    getStockByLotNodeValue("LIN", lin)

                                }
                            }
                        }
                        stocksByLotList?.postValue(alStockByLot)
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
    private fun getStockByLotNodeValue(tag: String?, element: Element) {
        if (element.hasChildNodes()) {
            var stock = StockByLot()
            for (i in 0 until element.childNodes.length) {

                val child: Element = element.childNodes.item(i) as Element
                if (child.hasChildNodes()) {
                    var fields = child.firstChild
                    if (child.attributes.item(0).nodeValue.equals("EXPDT"))
                        stock.expDate = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("LOT"))
                        stock.lot = fields.nodeValue
                    if (child.attributes.item(0).nodeValue.equals("SLOT"))
                        stock.slot = fields.nodeValue
                }
            }
            alStockByLot?.add(stock)

        }

    }

    fun filterStockLot(stockByLot: StockByLot, isFilterCleared: Boolean) {
        filteredStockByLot = stockByLot
        var alStockByLotNew: MutableList<StockByLot> = mutableListOf()
        if (isFilterCleared) {
            alStockByLotNew = alStockByLot
        } else {
            if (alStockByLot != null) {
                alStockByLot?.forEach {

                    if ((stockByLot.lot.isNotEmpty() && it?.lot?.equals(
                            stockByLot.lot,
                            ignoreCase = true
                        )) ||
                        (stockByLot.slot?.isNotEmpty()!! && it?.slot?.equals(
                            stockByLot.slot,
                            ignoreCase = true
                        )!!) ||
                        (stockByLot.expDate?.isNotEmpty()!! && getDate(stockByLot?.expDate!!)?.before(getDate(it.expDate!!))!!)
                    ) {
                        alStockByLotNew.add(it)
                    }
                }
            }
        }


        stocksByLotList?.postValue(alStockByLotNew)
    }

    fun createStockChange(siteId:String,txn : String, products: List<StockChange>) {
        if (!hasNetwork()) {
            errorMessage = appContext.getString(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.createStockChange(
                    XMLCreateStockChangeRequest(siteId,txn,products,"CWSSCS")
                )
                if (response != null) {
                    if (response?.rootElement2?.query?.queryReturn?.resultXml != null) {
                        Log.d("Messages", response?.rootElement2?.query?.queryReturn?.resultXml!!)
                        // Steps to convert this input stream into a list
                        val builderFactory: DocumentBuilderFactory =
                            DocumentBuilderFactory.newInstance()
                        val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
                        var status = response?.rootElement2?.query?.queryReturn?.status
                        if (status == 1) {
                            var message = ""
                            if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                                message =
                                    response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                            }
                            successMessage?.postValue("Stock change created $message")

                            listener?.onSuccess()
                            deleteAllStock()
                        } else {
                            if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                                errorMessage =
                                    response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                                listener?.onFailure()
                            }
                        }
                    } else {
                        if (response?.rootElement2?.query?.queryReturn?.messages != null && response?.rootElement2?.query?.queryReturn?.messages?.isNotEmpty()!!) {
                            errorMessage =
                                response?.rootElement2?.query?.queryReturn?.messages?.get(0)?.message!!
                            listener?.onFailure()
                        }

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

}