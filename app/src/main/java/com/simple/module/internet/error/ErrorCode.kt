package com.simple.module.internet.error

class ErrorCode {
    companion object{
        const val CODE_OK=200
        const val CODE_UNKNOWN=996
        const val CODE_FAILED=997
        const val CODE_TRANSFORM_ERROR=998
    }
}
class Error(val code:Int,message:String?,e:Throwable?):Throwable(message,e){

}