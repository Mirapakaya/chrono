package com.chronoio.chrono.device.base

import com.chronoio.chrono.data.auth.other.Device
import com.chronoio.chrono.device.download.AndroidDeviceDownloader

class AndroidDevice(
    private val downloader: AndroidDeviceDownloader
) : Device {
    override fun download(url: String, name: String) = downloader.download(url = url, name = name)
}