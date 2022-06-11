package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLProductQuantityRequest(var product:String) {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody(product)

    @Root
    class ProductsRequestBody(var product:String) {

        @field:Element(name = "wss:read")
        var query = QueryRequest(product)

        @Root
        class QueryRequest(var product:String) {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "ZITM"
            @field:Element(name = "objectKeys")
            var objectKeys =
                ObjectKeys(
                    product
                )

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

            @Root
            class ObjectKeys(var product:String) {
                @field:Attribute(name = " xsi:type")
                private val type: String = "wss:ArrayOfCAdxParamKeyValue"

                @field:Attribute(name = " soapenc:arrayType")
                private val arrayType: String = "wss:CAdxParamKeyValue[]"

                @field:Element(name = "Key")
                var key: String = "ITMREF"

                @field:Element(name = "Value")
                var value: String = product
            }

        }

    }

}