package com.retail.catalog

class CatalogRepository {

  def get() = {
    new Catalog(Seq(new CatalogItem("1", "book1", "/book1.png", BigDecimal("100.12")),
                    new CatalogItem("2", "book2", "/book2.png", BigDecimal("90.12"))))
  }
}
