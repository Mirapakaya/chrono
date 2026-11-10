package com.chronoio.chrono.domain.download

import com.chronoio.chrono.core_models.Url

/**
 * Base interface for downloaders.
 */
interface Downloader {
    /**
     * Starts downloading file from url.
     * @param name file name
     * @param url url of the file to download
     */
    fun download(url: Url, name: String)
}