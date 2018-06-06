/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;

/**
 * AES签名工具
 */
public class SigAESUtil {

	public static final String SIG_ENCODING = "UTF-8";
	public static final String HASH_ALG_HMACSHA256 = "HmacSHA256";
	public static final String HASH_ALG_HMACSHA1 = "HmacSHA1";
	public static final String CRYPT_ALG_AES = "AES";
	public static final String CRYPT_ALG_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
	/** 种子失败时*/
	private static final String SALT_FAILED = "999999999999";
	/** 分号 */
	private static final String SEMICOLON = ";";
	/** 加密成功 */
	private static final String ENCRESULTCODE_OK = "0";
	/** 加密失败 */
	private static final String ENCRESULTCODE_NG = "1";
	/** IV长度 */
	private static final int IV_LENGTH = 16;
	/** salt长度 */
	private static final int SALT_LENGTH = 8;

	public enum EncodingType {
		BASE64, HEX
	}

	// 当クラスで使う可逆の暗号化アルゴリズムの種類
	// 暗号アルゴリズム => AES（Rijndael）
	// 秘密鍵 => secretKey
	// 秘密鍵の長さ => 128bit
	// ブロック暗号化モード => CBC
	// IV => 0000000000000000
	// padding方式 => PKCS5Padding

	// 機密情報をDBに格納するときの暗号化の下となる文字列。最終的にはファイルシステム上の情報と併せて利用すべき。
	// また、個人のパスワードなどはそもそも可逆の暗号でDBに格納してはいけない。非可逆なハッシュ値のみを格納すること。
	// AESの鍵長は128bit（16byte）。
	public static String COMMON_AES_SALT = "gWdUa9Tw22epAb7j";
	// CBC用のIV（Initialization Vector：初期化ベクトル）
	private static String COMMON_AES_IV = "0000000000000000";
	// 暗号化の鍵を復号化する用秘密s鍵
	public static String KEY_AES_SALT = "EuDhp3ypxyLJsDdg";

	public static String getSignatureHMACSHA256Base64(String srcString,
			String secretKey) {
		try {
			String signature = getSignature(srcString, secretKey,
					HASH_ALG_HMACSHA256, EncodingType.BASE64);
			return signature;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getSignatureHMACSHA1Base64(String srcString,
			String secretKey) {
		try {
			String signature = getSignature(srcString, secretKey,
					HASH_ALG_HMACSHA1, EncodingType.BASE64);
			return signature;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getSignature(String srcString, String secretKey,
			String algorithm, EncodingType encodingType)
			throws InvalidKeyException, NoSuchAlgorithmException {
		Key key = new SecretKeySpec(secretKey.getBytes(Charset
				.forName(SIG_ENCODING)), algorithm);
		Mac mac = Mac.getInstance(key.getAlgorithm());
		mac.init(key);
		byte[] srcBytes = srcString.getBytes(Charset.forName(SIG_ENCODING));
		byte[] bs = mac.doFinal(srcBytes);
		switch (encodingType) {
		case BASE64:
			return StringUtil.byteToBase64(bs);
		case HEX:
			return StringUtil.byteToHexString(bs);
		default:
			return null;
		}
	}

	public static boolean checkSignatureHMACSHA256Base64(String targetString,
			String signature, String secretKey) {
		try {
			String actual = getSignatureHMACSHA256Base64(targetString,
					secretKey);
			return actual.equals(signature);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkSignatureHMACSHA1Base64(String targetString,
			String signature, String secretKey) {
		try {
			String actual = getSignatureHMACSHA1Base64(targetString, secretKey);
			return actual.equals(signature);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkSignature(String targetString, String signature,
			String secretKey, String algorithm, EncodingType encodingType)
			throws InvalidKeyException, NoSuchAlgorithmException {
		try {
			String actual = getSignature(targetString, secretKey, algorithm,
					encodingType);
			return actual.equals(signature);
		} catch (Exception e) {
			return false;
		}
	}

	public static String encryptAES(String srcString, String secretKey)
			throws GeneralSecurityException, NoSuchAlgorithmException {
		return StringUtil.byteToBase64(encryptAESasByte(srcString, secretKey));
	}

	public static byte[] encryptAESasByte(String srcString, String secretKey)
			throws GeneralSecurityException, NoSuchAlgorithmException {
		// ciper.initは重い処理のようだが、ひとまず都度インスタンス化する。
		// 暗号化時と復号化時で同じパラメータを渡せるならAES/CBC/PKCS5Paddingがよいが、ひとまずデフォルトで実施。
		String algorithm = CRYPT_ALG_AES;
		String algorithmWithMode = CRYPT_ALG_AES_CBC_PKCS5PADDING;
		Key key = new SecretKeySpec(secretKey.getBytes(Charset
				.forName(SIG_ENCODING)), algorithm);
		IvParameterSpec iv = new IvParameterSpec(COMMON_AES_IV.getBytes(Charset
				.forName(SIG_ENCODING)));
		Cipher ciper = Cipher.getInstance(algorithmWithMode);
		ciper.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] srcBytes = srcString.getBytes(Charset.forName(SIG_ENCODING));
		return ciper.doFinal(srcBytes);
	}

	public static String decryptAES(String srcString, String secretKey)
			throws GeneralSecurityException, NoSuchAlgorithmException {
		return decryptAES(StringUtil.base64ToByte(srcString), secretKey);
	}

	public static String decryptAES(byte[] srcBytes, String secretKey)
			throws GeneralSecurityException, NoSuchAlgorithmException {
		// ciper.initは重い処理のようだが、ひとまず都度インスタンス化する。
		// 暗号化時と復号化時で同じパラメータを渡せるならAES/CBC/PKCS5Paddingがよいが、ひとまずデフォルトで実施。
		String algorithm = CRYPT_ALG_AES;
		String algorithmWithMode = CRYPT_ALG_AES_CBC_PKCS5PADDING;
		Key key = new SecretKeySpec(secretKey.getBytes(Charset
				.forName(SIG_ENCODING)), algorithm);
		IvParameterSpec iv = new IvParameterSpec(COMMON_AES_IV.getBytes(Charset
				.forName(SIG_ENCODING)));
		Cipher ciper = Cipher.getInstance(algorithmWithMode);
		ciper.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(ciper.doFinal(srcBytes),
				Charset.forName(SIG_ENCODING));
	}

	/**
	 * 暗号化済UserIdの生成
	 * 
	 * @param UserId
	 *            UserId
	 * @param encId
	 *            暗号化の鍵を識別するID
	 * @param encKey
	 *            暗号化の鍵
	 * @return 暗号化済UserId
	 */
	public static String encryptUserId(String UserId, String encId,
			String encKey) {

		// 暗号文
		String obUserId = null;
		// salt
		String salt = null;
		// 暗号化の鍵を識別するIDがnull,又は,暗号化の鍵がnullの場合
		if (StringUtil.isEmpty(encId) || StringUtil.isEmpty(encKey)) {
			return getXMtiobUserId(salt, obUserId, null);
		}

		// keyを取得
		byte[] key = decodeSecretKey(encKey);
		if (key == null || key.length == 0) {
			return getXMtiobUserId(salt, obUserId, encId);
		}

		// salt 生成
		salt = RandomUtil.getMD5String(RandomUtil.getUUIDString())
				.substring(0, SALT_LENGTH);
		if (StringUtil.isEmpty(salt)) {
			return getXMtiobUserId(SALT_FAILED, obUserId, encId);
		}
		byte[] saltArr = null;
		try {
			saltArr = salt.getBytes(SIG_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return getXMtiobUserId(SALT_FAILED, obUserId, encId);
		}

		// 鍵とsaltよりIVを取得する。
		byte[] iv = getIV(key, saltArr);
		if (iv == null || iv.length == 0) {
			return getXMtiobUserId(salt, obUserId, encId);
		}

		// IVと鍵より、UserIdを暗号化し、暗号文(obUserId)を取得する。
		obUserId = getObUserId(UserId, key, iv);

		return getXMtiobUserId(salt, obUserId, encId);
	}

	/**
	 * 暗号化済UserIdからのUserIdの復号
	 * 
	 * @param encryptUserId
	 *            暗号化済UserId
	 * @param encId
	 *            暗号化の鍵を識別するID
	 * @param encKey
	 *            暗号化の鍵
	 * @return 復号済UserId
	 */
	public static String decryptUserId(String encryptUserId, String encId,
			String encKey) {
		// 暗号化済UserId
		String[] encryptUserIdInfo = encryptUserId.split(SEMICOLON);
		if (encryptUserIdInfo.length != 4) {
			return null;
		} else {
			// 暗号文(obUserId)を取得する。
			String obUserId = encryptUserIdInfo[2];
			// <obUserId>が空の場合、暗号化は失敗しているものとして、処理を終了する。
			if (StringUtil.isEmpty(obUserId)) {
				return null;
			}

			// 暗号化済UserId：<encid>より、鍵を取得する。
			String encidStr = encryptUserIdInfo[0];
			String encKeyStr = StringUtil.EMPTY_STR;
			if (encidStr.equals(encId)) {
				encKeyStr = encKey;
			}
			// keyを取得
			byte[] key = decodeSecretKey(encKeyStr);

			// 暗号化済UserId：<salt>より、saltを取得する。
			byte[] salt = StringUtil.base64ToByte(encryptUserIdInfo[1]);

			// 鍵とsaltよりIVを取得する。
			byte[] iv = getIV(key, salt);

			try {
				return decryptAESasByte(obUserId, key, iv);
			} catch (GeneralSecurityException e) {
				return null;
			}
		}

	}

	public static String encryptPassWord(String passWord) {
		String encryptPassWord = null;
		try {
			encryptPassWord = encryptAES(passWord, COMMON_AES_IV);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return encryptPassWord;

	}

	public static String decodePassWord(String enPassWord) {
		String decodePassWord = null;
		try {
			decodePassWord = decryptAES(StringUtil.base64ToByte(enPassWord),
					COMMON_AES_IV);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return decodePassWord;
	}

	/**
	 * 暗号化済UserId= <encid>+";"+<salt>+";"+<obUserId>+";"+<encresultcode>を作成
	 * 
	 * @param salt
	 * @param obUserId
	 * @param encId
	 * @return String
	 */
	private static String getXMtiobUserId(String salt, String obUserId,
			String encId) {
		StringBuilder encryptUserIdSb = new StringBuilder();

		if (StringUtil.isNotEmpty(encId)) {
			encryptUserIdSb.append(encId).append(SEMICOLON);
		} else {
			encryptUserIdSb.append(SEMICOLON);
		}

		if (StringUtil.isNotEmpty(salt)) {
			encryptUserIdSb.append(
					StringUtil.byteToBase64(salt.getBytes(Charset
							.forName(SIG_ENCODING)))).append(SEMICOLON);
		} else {
			encryptUserIdSb.append(SEMICOLON);
		}

		if (StringUtil.isNotEmpty(obUserId)) {
			encryptUserIdSb.append(obUserId).append(SEMICOLON)
					.append(ENCRESULTCODE_OK);
		} else {
			encryptUserIdSb.append(SEMICOLON).append(ENCRESULTCODE_NG);
		}
		return encryptUserIdSb.toString();
	}

	/**
	 * 暗号文UserIdを取得
	 * 
	 * @param UserId
	 *            UserId 平文
	 * @param key
	 *            暗号キー
	 * @param iv
	 *            iv
	 * @return 暗号文UserId
	 */
	private static String getObUserId(String UserId, byte[] key, byte[] iv) {
		// IVと鍵より、UserIdを暗号化し、暗号文(obUserId)を取得する。
		String obUserId = null;
		if (StringUtil.isEmpty(UserId) || key == null || key.length == 0
				|| iv == null) {
			return null;
		}
		try {
			obUserId = encryptAESasByte(UserId, key, iv);
		} catch (GeneralSecurityException e) {
		}
		return obUserId;
	}

	/**
	 * 鍵とsaltよりIVを取得する。 IV = MD5(鍵+salt)
	 * 
	 * @param key
	 *            鍵
	 * @param salt
	 *            salt
	 * @return IV
	 */
	protected static byte[] getIV(byte[] key, byte[] salt) {
		if (key == null || key.length == 0 || salt == null || salt.length == 0) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key);
			md.update(salt);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 暗号化鍵として復号化する
	 * 
	 * @param secretKey
	 *            暗号化鍵
	 * @return 復号化鍵
	 */
	private static byte[] decodeSecretKey(String secretKey) {
		if (StringUtil.isEmpty(secretKey)) {
			return null;
		}
		try {
			return StringUtil.base64ToByte(decryptAES(secretKey, KEY_AES_SALT));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 暗号文(obUserId)を取得する。 PKCS#5パディングを施したものをiv(IV)とkey(鍵)で AES-256-CBC
	 * により暗号化したもの。
	 * 
	 * @param srcString
	 *            平文
	 * @param secretKey
	 *            暗号キー
	 * @param strIv
	 *            iv
	 * @return UserId暗号化設定ファイル情報
	 * @throws GeneralSecurityException
	 *             GeneralSecurityException
	 */
	private static String encryptAESasByte(String srcString, byte[] secretKey,
			byte[] strIv) throws GeneralSecurityException {
		Key key = new SecretKeySpec(secretKey, CRYPT_ALG_AES);
		IvParameterSpec iv = new IvParameterSpec(strIv, 0, IV_LENGTH);
		Cipher ciper = Cipher.getInstance(CRYPT_ALG_AES_CBC_PKCS5PADDING);
		ciper.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] srcBytes = srcString.getBytes(Charset.forName(SIG_ENCODING));
		return StringUtil.byteToBase64(ciper.doFinal(srcBytes));
	}

	/**
	 * IVと鍵より、暗号文を復号し、UserIdを取得する。 PKCS#5パディングを施したものをiv(IV)とkey(鍵)で AES-256-CBC
	 * により暗号化したもの。
	 * 
	 * @param srcString
	 *            暗号文
	 * @param secretKey
	 *            暗号キー
	 * @param strIv
	 *            iv
	 * @return UserId暗号化設定ファイル情報
	 * @throws GeneralSecurityException
	 *             GeneralSecurityException
	 */
	private static String decryptAESasByte(String srcString, byte[] secretKey,
			byte[] strIv) throws GeneralSecurityException {
		Key key = new SecretKeySpec(secretKey, CRYPT_ALG_AES);
		IvParameterSpec iv = new IvParameterSpec(strIv, 0, IV_LENGTH);
		Cipher ciper = Cipher.getInstance(CRYPT_ALG_AES_CBC_PKCS5PADDING);
		ciper.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(ciper.doFinal(StringUtil.base64ToByte(srcString)),
				Charset.forName(SIG_ENCODING));
	}
}
