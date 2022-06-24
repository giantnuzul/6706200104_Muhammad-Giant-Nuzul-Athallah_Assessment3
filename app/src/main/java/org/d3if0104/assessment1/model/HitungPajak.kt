package org.d3if0104.assessment1.model

import org.d3if0104.assessment1.db.PajakEntity

fun PajakEntity.hitung(): HasilPajak {
    val ppn = (Harga * Jumlah) * 10/100
    val total = ppn + (Harga * Jumlah)
    return HasilPajak(ppn, total)
}