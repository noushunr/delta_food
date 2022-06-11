package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class XMLSitesRequest() {

    @field:Element(name = "soapenv:Body")
    var rootElement2 = ProductsRequestBody()

    @Root
    class ProductsRequestBody() {

        @field:Element(name = "wss:run")
        var query = QueryRequest()

        @Root
        class QueryRequest() {
            @field:Element(name = "callContext")
            var callContext = CallContext()
            @field:Element(name = "publicName")
            var publicName = "ZFCYLST"
            @field:Element(data = true)
            var inputXml =  "<PARAM>\n<FLD NAM=\"ITMREF\" >\"\"</FLD>\n<FLD NAM=\"STOFCY\" >\"\"</FLD>\n</PARAM>"

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