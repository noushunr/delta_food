package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLUnitProuctsRequest(var productId:String, var functionName: String) {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody(productId,functionName)

    @Root
    class ProductsRequestBody(var productId:String, var functionName: String) {

        @field:Element(name = "wss:run")
        var query = QueryRequest(productId,functionName)

        @Root
        class QueryRequest(var productId:String, var functionName: String) {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "$functionName"
            @field:Element(data = true)
            var inputXml =  "<PARAM><GRP ID=\"GRP1\"><FLD NAME=\"PRODUCT\">$productId</FLD></GRP></PARAM>"

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