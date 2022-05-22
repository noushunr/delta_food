package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLPurchaseOrderSearchRequest(var site:String, var poNo:String, var date:String) {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody(site,poNo,date)

    @Root
    class ProductsRequestBody(var site:String,var poNo:String,var date:String) {

        @field:Element(name = "wss:read")
        var query = QueryRequest(site,poNo,date)

        @Root
        class QueryRequest(var site:String,var poNo:String,var date:String) {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "POH"
            @field:Element(data = true)
            var inputXml =  "<PARAM><FLD NAM=\"SITE\">$site</FLD><FLD NAM=\"PRODUCT\">$poNo</FLD><FLD NAM=\"PODATE\">$date</FLD></PARAM>"

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

            }

        }

    }

}