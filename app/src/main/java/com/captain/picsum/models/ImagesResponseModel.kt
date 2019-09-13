package com.captain.picsum.models

data class ImagesResponseModel(val format:String?,
                               val width:Int?,
                               val height:Int?,
                               val filename:String?,
                               val id:Int?,
                               val author:String?,
                               val author_url:String?,
                               val post_url:String?
                               ) {
}