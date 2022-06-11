import com.deltafood.data.model.response.TechnicalInfos
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope",strict = false)
class XMLStockCreateResponse {
    @field:Element(name = "Body")
    var rootElement2: Body? = null

    @Root(name = "soapenv:Body",strict = false)
    class Body {
        @field:ElementList(required = false, inline = true)
        var messages: MutableList<QueryResponse.QueryReturn.Messages>? = null
        @field:Element(name = "saveResponse")
        var query : QueryResponse? = null
        @Root(name = "wss:saveResponse",strict = false)
        class QueryResponse {
            @field:Attribute(name = "encodingStyle")
            var encodingStyle :String ?=null
            @field:Element(name = "saveReturn")
            var queryReturn : QueryReturn? = null
            @Root(strict = false)
            class QueryReturn {
                @field:Attribute(name = "type")
                var type :String ?=null
//                @field:Element(required = false,name = "messages")
//                var messages : Messages? = null

                @field:ElementList(required = false, inline = true)
                var messages: MutableList<Messages>? = null
//                @field:ElementList(
//                    required = false,
//                    name = "messages",
//                    entry = "messages",
//                    inline = true,
//                    empty = true
//                )
//                var messages: MutableList<Messages>? = null
                @field:Element(data = true,required = false)
                var resultXml :String ?=null
                @field:Element(name = "technicalInfos")
                var technicalInfos : TechnicalInfos? = null
                @field:Element(name = "status")
                var status :Int = 0
                @Root(name="messages")
                class Messages {
                    @field:Element(name = "type")
                    var type : Int? = null
                    @field:Element(name = "message")
                    var message : String? = null
                }
//                @Root(strict = false)
//                class RESULTXML {
//                    @field:Attribute(name = "type")
//                    var type :String ?=null
//                    @field:Element(name = "RESULT")
//                    var RESULT : RESULT? = null
////                    @field:Element(name = "RESULT")
////                    var text: String? = null
//                }
//                @Root(name = "RESULT",strict = false)
//                class RESULT {
//                    @field:Element(name = "GRP")
//                    var grp : GRP? = null
//                    @field:Element(name = "TAB")
//                    var tab : GRP? = null
//                }
//                @Root
//                class GRP {
//                    @field:Element(name = "FLD")
//                    var fld : String? = null
//                }
//                @Root
//                class TAB {
//                    @field:Element(name = "FLD")
//                    var fld : String? = null
//                }
            }
        }
    }

}