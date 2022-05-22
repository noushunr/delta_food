package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLSearchPoInquiriesListRequest(var site:String) {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody(site)

    @Root
    class ProductsRequestBody(var site:String) {

        @field:Element(name = "wss:read")
        var query = QueryRequest(site)

        @Root
        class QueryRequest(var site:String) {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "ZPOH"
            @field:Element(name = "objectKeys")
            var objectKeys = ObjectKeys(site)
//            @field:Element(data = true)
//            var inputXml =  "<PARAM><GRP ID=\"GRP1\"><FLD NAME=\"SITE\">$site</FLD></GRP></PARAM>"

            @Root
            class ObjectKeys(var pohNo:String) {
                @field:Attribute(name = " xsi:type")
                 var type= "wss:ArrayOfCAdxParamKeyValue"
                @field:Attribute(name = " soapenc:arrayType")
                 var arrayType = "wss:CAdxParamKeyValue[]"
                @field:Element(name = "Key")
                var key: String = "POHNUM"
                @field:Element(name = "Value")
                var value: String = pohNo
            }
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