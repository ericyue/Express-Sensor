package info.ericyue.es.zxing.client.android;

import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
/**
 * Handles any locale-specific logic for the client.
 *
 * @author Sean Owen
 */
public final class LocaleManager {
  private static final String DEFAULT_TLD = "com";

  // Locales where Google web search is available. These should be kept in sync with our
  // translations. The format for the manual countries is:
  // Language, Country, unused, Google domain suffix
  private static final Map<Locale,String> GOOGLE_COUNTRY_TLD;
  static {
    GOOGLE_COUNTRY_TLD = new HashMap<Locale,String>();
    GOOGLE_COUNTRY_TLD.put(new Locale("en", "AU", ""), "com.au"); // AUSTRALIA
    GOOGLE_COUNTRY_TLD.put(Locale.CHINA, "cn");
    GOOGLE_COUNTRY_TLD.put(Locale.UK, "co.uk");
  }

  // Google Product Search for mobile is available in fewer countries than web search. See here:
  // http://www.google.com/support/merchants/bin/answer.py?answer=160619
  private static final Map<Locale,String> GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD;
  static {
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD = new HashMap<Locale,String>();
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(new Locale("en", "AU", ""), "com.au"); // AUSTRALIA
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.CHINA, "cn");
    GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.UK, "co.uk");
  }

  // Book search is offered everywhere that web search is available.
  private static final Map<Locale,String> GOOGLE_BOOK_SEARCH_COUNTRY_TLD;
  static {
    GOOGLE_BOOK_SEARCH_COUNTRY_TLD = new HashMap<Locale,String>();
    GOOGLE_BOOK_SEARCH_COUNTRY_TLD.putAll(GOOGLE_COUNTRY_TLD);
  }

  private LocaleManager() {}

  /**
   * @return country-specific TLD suffix appropriate for the current default locale
   *  (e.g. "co.uk" for the United Kingdom)
   */
  public static String getCountryTLD() {
    return doGetTLD(GOOGLE_COUNTRY_TLD);
  }

  /**
   * The same as above, but specifically for Google Product Search.
   * @return The top-level domain to use.
   */
  public static String getProductSearchCountryTLD() {
    return doGetTLD(GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD);
  }

  /**
   * The same as above, but specifically for Google Book Search.
   * @return The top-level domain to use.
   */
  public static String getBookSearchCountryTLD() {
    return doGetTLD(GOOGLE_BOOK_SEARCH_COUNTRY_TLD);
  }

  /**
   * Does a given URL point to Google Book Search, regardless of domain.
   *
   * @param url The address to check.
   * @return True if this is a Book Search URL.
   */
  public static boolean isBookSearchUrl(String url) {
    return url.startsWith("http://google.com/books") || url.startsWith("http://books.google.");
  }

  private static String doGetTLD(Map<Locale,String> map) {
    Locale locale = Locale.getDefault();
    if (locale == null) {
      return DEFAULT_TLD;
    }
    String tld = map.get(locale);
    if (tld == null) {
      return DEFAULT_TLD;
    }
    return tld;
  }
}
