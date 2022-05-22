import com.deltafood.data.model.request.XMLProductRequest
import com.deltafood.data.model.response.TechnicalInfos
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root(name = "soapenv:Envelope")
class ProductResponse {
    @field:Element(name = "Body")
    var rootElement2: Body? = null

    @Root(name = "soapenv:Body")
    class Body {

        @field:Element(name = "readResponse")
        var query : QueryResponse? = null
        @Root(name = "wss:readResponse")
        class QueryResponse {
            @field:Attribute(name = "encodingStyle")
            var encodingStyle :String ?=null
            @field:Element(name = "readReturn")
            var queryReturn : QueryReturn? = null
            @Root
            class QueryReturn {
                @field:Attribute(name = "type")
                var type :String ?=null
                @field:Element(required = false)
                var messages : Messages? = null
                @field:Element(data = true,required = false)
                var resultXml :String ?=null
                @field:Element(name = "technicalInfos")
                var technicalInfos : TechnicalInfos? = null
                @field:Element(name = "status")
                var status :Int = 0
                @Root
                class Messages {
                    @field:Element(name = "type")
                    var type : Int? = null
                    @field:Element(name = "message")
                    var message : String? = null
                }
            }
        }
    }

}