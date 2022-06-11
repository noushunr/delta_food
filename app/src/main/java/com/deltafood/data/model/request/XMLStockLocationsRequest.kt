package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLStockLocationsRequest(val product:String, val site : String) {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody(product, site)

    @Root
    class ProductsRequestBody(val product:String,val site : String) {

        @field:Element(name = "wss:run")
        var query = QueryRequest(product,site)

        @Root
        class QueryRequest(val product:String,val site : String) {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "ZITMLOC"
            @field:Element(data = true)
            var inputXml =  "<PARAM><GRP ID=\"GRP1\"><FLD NAME=\"PRODUCT\">$product</FLD><FLD NAME=\"SITE\">$site</FLD></GRP></PARAM>"

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