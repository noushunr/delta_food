package com.deltafood.data.model.response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Noushad N on 10-05-2022.
 */
@Root
class TechnicalInfos {
    @field:Attribute(name = "type")
    var type :String ?=null
    @field:Element(name = "busy")
    var busy : Boolean? = false
    @field:Element(name = "changeLanguage")
    var changeLanguage : Boolean? = false
    @field:Element(name = "changeUserId")
    var changeUserId : Boolean? = false
    @field:Element(name = "flushAdx")
    var flushAdx : Boolean? = false
    @field:Element(name = "loadWebsDuration")
    var loadWebsDuration : Double? = 0.0
    @field:Element(name = "nbDistributionCycle")
    var nbDistributionCycle : Int? = -1
    @field:Element(name = "poolDistribDuration")
    var poolDistribDuration : Double? = 0.0
    @field:Element(name = "poolEntryIdx")
    var poolEntryIdx : Int? = 0
    @field:Element(name = "poolExecDuration")
    var poolExecDuration : Int? = -1
    @field:Element(name = "poolRequestDuration")
    var poolRequestDuration : Int? = -1
    @field:Element(name = "poolWaitDuration")
    var poolWaitDuration : Int? = -1
    @field:Element(required = false)
    var processReport : String? = null
    @field:Element(name = "processReportSize")
    var processReportSize : Int? = -1
    @field:Element(name = "reloadWebs")
    var reloadWebs : Boolean? = false
    @field:Element(name = "resumitAfterDBOpen")
    var resumitAfterDBOpen : Boolean? = false
    @field:Element(required = false)
    var rowInDistribStack :Int?=0
    @field:Element(name = "totalDuration")
    var totalDuration : Double? = 0.0
    @field:Element(required = false)
    var traceRequest : String?=null
    @field:Element(name = "traceRequestSize")
    var traceRequestSize : Int? = 0
}