package com.chronoio.chrono.data.auth.other

import com.chronoio.chrono.core_models.Url
import com.chronoio.chrono.domain.download.Downloader

class DataDownloader(private val device: Device) : Downloader {

    override fun download(url: Url, name: String) = device.download(url, name)
}