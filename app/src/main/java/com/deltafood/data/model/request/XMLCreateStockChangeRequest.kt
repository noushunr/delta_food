package com.deltafood.data.model.request

import com.deltafood.database.entities.StockChange
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLCreateStockChangeRequest(var siteId:String,var txn:String,var products : List<StockChange>, functionName: String) {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody(siteId,txn,products,functionName)

    @Root
    class ProductsRequestBody(var siteId:String,var txn:String,var products : List<StockChange>, var functionName: String) {

        @field:Element(name = "wss:save")
        var query = QueryRequest(siteId, txn,products, functionName)

        @Root
        class QueryRequest(var siteId:String,var txn:String,var products : List<StockChange>, var functionName: String) {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "$functionName"
            @field:Element(data = true)
            var objectXml =  getDetails(siteId, txn,products)

            @Root
            class CallContext {
                @field:Attribute(name = " xsi:type")
                var type = "wss:CAdxCallContext"
                @field:Element(name = "codeLang")
                var codeLanguage = "ENG"
                @field:Element(name = "poolAlias")
                var poolAlias = "SEED"
                @field:Element(name = "poolId")
                var poolId = "SEED"
//                @field:Element(name = "requestConfig")
//                var requestConfig = "adxwss.optreturn=XML&adxwss.beautify=true&adxwss.trace.on=off"

            }

            private fun getDetails(siteId:String, txn:String,products : List<StockChange>):String{
                var output = ""
                output = "<PARAM><GRP ID=\"SCS1_1\"><FLD NAME=\"STOFCY\" TYPE=\"Char\">$siteId</FLD><FLD NAME=\"SGHTYP\" TYPE=\"Char\">$txn</FLD><FLD MENULAB=\"Internal\" MENULOCAL=\"792\" NAME=\"BETFCYCOD\" TYPE=\"Integer\">1</FLD>" +
                        "<FLD NAME=\"VCRNUM\" TYPE=\"Char\"></FLD><FLD NAME=\"VCRDES\" TYPE=\"Char\"/><FLD NAME=\"IPTDAT\" TYPE=\"Date\">20220308</FLD><FLD NAME=\"PJT\" TYPE=\"Char\"/><FLD NAME=\"TRSFAM\" TYPE=\"Char\"/><FLD NAME=\"TRSCOD\" " +
                        "TYPE=\"Char\"/><FLD MENULAB=\"No\" MENULOCAL=\"1\" NAME=\"LOCCHG\" TYPE=\"Integer\">1</FLD><FLD MENULAB=\"No\" MENULOCAL=\"1\" NAME=\"STACHG\" TYPE=\"Integer\">1</FLD><FLD MENULAB=\"No\" MENULOCAL=\"1\" NAME=\"PCUCHG\" " +
                        "TYPE=\"Integer\">1</FLD></GRP><TAB DIM=\"600\" ID=\"SCS1_7\" SIZE=\"${products.size}\">"
                products?.forEachIndexed { index, stockChange ->
                    output += "<LIN NUM=\"${index+1}\"><FLD NAME=\"ITMREF\" TYPE=\"Char\">${stockChange.productId}</FLD><FLD NAME=\"ITMDES1\" TYPE=\"Char\">${stockChange.productName}</FLD><FLD NAME=\"LOT\" TYPE=\"Char\">${stockChange.lot}</FLD><FLD NAME=\"SLO\" TYPE=\"Char\">${stockChange.sIo}</FLD>" +
                            "<FLD NAME=\"STA\" TYPE=\"Char\">${stockChange.status}</FLD><FLD NAME=\"LPNNUM\" TYPE=\"Char\"/><FLD NAME=\"LOC\" TYPE=\"Char\">${stockChange.fromLo}</FLD><FLD NAME=\"PCU\" TYPE=\"Char\">${stockChange.unit}</FLD><FLD NAME=\"QTYPCU\" TYPE=\"Decimal\">1</FLD>" +
                            "<FLD NAME=\"PCUSTUCOE\" TYPE=\"Decimal\">1</FLD><FLD NAME=\"QTYSTUDES\" TYPE=\"Decimal\">1</FLD><FLD NAME=\"SERNUM\" TYPE=\"Char\">${stockChange.serial}</FLD><FLD NAME=\"PALNUM\" TYPE=\"Char\"/><FLD NAME=\"CTRNUM\" TYPE=\"Char\"/>" +
                            "<FLD NAME=\"STOFLD1\" TYPE=\"Char\"/><FLD NAME=\"STOFLD2\" TYPE=\"Char\"/><FLD NAME=\"QLYCTLDEM\" TYPE=\"Char\"/><FLD NAME=\"PCUDES\" TYPE=\"Char\">${stockChange.unit}</FLD><FLD NAME=\"COEDES\" TYPE=\"Decimal\">1</FLD>" +
                            "<FLD NAME=\"STADES\" TYPE=\"Char\">${stockChange.toSta}</FLD><FLD NAME=\"LOCDES\" TYPE=\"Char\">${stockChange.loc}</FLD><FLD NAME=\"PALNUMDES\" TYPE=\"Char\"/><FLD NAME=\"CTRNUMDES\" TYPE=\"Char\"/><FLD NAME=\"MVTDES\" TYPE=\"Char\"/>" +
                            "<FLD NAME=\"CCE1\" TYPE=\"Char\"/><FLD NAME=\"CCE2\" TYPE=\"Char\"/><FLD NAME=\"CCE3\" TYPE=\"Char\"/><FLD NAME=\"CCE4\" TYPE=\"Char\"/><FLD NAME=\"CCE5\" TYPE=\"Char\"/><FLD NAME=\"CCE6\" TYPE=\"Char\"/>" +
                            "<FLD NAME=\"CCE7\" TYPE=\"Char\"/><FLD NAME=\"CCE8\" TYPE=\"Char\"/><FLD NAME=\"CCE9\" TYPE=\"Char\"/><FLD NAME=\"CCE10\" TYPE=\"Char\"/><FLD NAME=\"CCE11\" TYPE=\"Char\"/><FLD NAME=\"CCE12\" TYPE=\"Char\"/>" +
                            "<FLD NAME=\"CCE13\" TYPE=\"Char\"/><FLD NAME=\"CCE14\" TYPE=\"Char\"/><FLD NAME=\"CCE15\" TYPE=\"Char\"/><FLD NAME=\"CCE16\" TYPE=\"Char\"/><FLD NAME=\"CCE17\" TYPE=\"Char\"/><FLD NAME=\"CCE18\" TYPE=\"Char\"/><FLD NAME=\"CCE19\" TYPE=\"Char\"/><FLD NAME=\"CCE20\" TYPE=\"Char\"/><FLD NAME=\"DISCRGVAL4\" TYPE=\"Decimal\">0</FLD><FLD NAME=\"DISCRGVAL5\" TYPE=\"Decimal\">0</FLD><FLD NAME=\"DISCRGVAL6\" TYPE=\"Decimal\">0</FLD></LIN>"
                }
                output += "</TAB></PARAM>"
                return output
            }

        }

    }

}