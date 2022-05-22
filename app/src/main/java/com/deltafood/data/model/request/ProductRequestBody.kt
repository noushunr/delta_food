package com.deltafood.data.model.request

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root
class ProductsRequestBody(pohNo:String) {

    @field:Element(name = "wss:read")
    var query: QueryRequest = QueryRequest(pohNo)
}

@Root
class QueryRequest(var pohNo:String) {
    @Element(name = "callContext")
    var callContext = CallContext()

    @Element(name = "publicName")
    var publicName: String = "ZPOH"

    @Element(name = "objectKeys")
    var objectKeys = ObjectKeys(pohNo)

    @Element(name = "listSize")
    var listSize: Int = 1000


}
@Root
class ObjectKeys(var pohNo:String) {
    @Attribute(name = " xsi:type")
    private val type: String = "wss:ArrayOfCAdxParamKeyValue"

    @Attribute(name = " soapenc:arrayType")
    private val arrayType: String = "wss:CAdxParamKeyValue[]"

    @Element(name = "Key")
    var key: String = "POHNUM"

    @Element(name = "Value")
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