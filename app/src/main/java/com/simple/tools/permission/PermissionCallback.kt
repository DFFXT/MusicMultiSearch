package com.simple.tools.permission

class PermissionCallback(val resCode: Int,var callback:((allGranted:Boolean)->Unit))