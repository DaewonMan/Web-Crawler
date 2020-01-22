package com.dwm.webcrawler.crawl;

public class CrawlData {
	private String country;
	private String state;
	private String district;
	private String city;
	private String zip;
	private String weather;
	private String coordinates;
	private String timezone;
	private String languages;
	private String currency;
	private String address;
	private String hostname;
	private String asn;
	private String isp;
	private String crawler;
	private String proxy;
	private String attack;
	private String code;
	private String ip;
	
	public CrawlData() {
		// TODO Auto-generated constructor stub
	}

	public CrawlData(String country, String state, String district, String city, String zip, String weather,
			String coordinates, String timezone, String languages, String currency, String address, String hostname,
			String asn, String isp, String crawler, String proxy, String attack, String code, String ip) {
		super();
		this.country = country;
		this.state = state;
		this.district = district;
		this.city = city;
		this.zip = zip;
		this.weather = weather;
		this.coordinates = coordinates;
		this.timezone = timezone;
		this.languages = languages;
		this.currency = currency;
		this.address = address;
		this.hostname = hostname;
		this.asn = asn;
		this.isp = isp;
		this.crawler = crawler;
		this.proxy = proxy;
		this.attack = attack;
		this.code = code;
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getCrawler() {
		return crawler;
	}

	public void setCrawler(String crawler) {
		this.crawler = crawler;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getAttack() {
		return attack;
	}

	public void setAttack(String attack) {
		this.attack = attack;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
