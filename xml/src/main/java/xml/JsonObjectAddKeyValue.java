package xml;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class JsonObjectAddKeyValue {
	public static void main(String[] args){

		Path path = Paths.get("enquete.xml");
		StringBuilder sb = new StringBuilder();
		try {
			//XMLの読込
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String value : lines) {
				sb.append(value);
			}
			System.out.println(XML.toJSONObject(sb.toString()));
			JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
			JSONObject root = xmlJSONObj.getJSONObject("EnqueteList");

			// 出力ファイルの作成
			FileWriter fw = new FileWriter("enquete.csv", false);
			// PrintWriterクラスのオブジェクトを生成
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

			// ヘッダーの指定
			pw.print("Id");
			pw.print(",");
			pw.print("VisitorId");
			pw.print(",");
			pw.print("DataId");
			pw.print(",");
			pw.print("HistoryId");
			pw.print(",");
			pw.print("設問");
			pw.print(",");
			pw.print("回答");
			pw.println();

			//test 開始
			Object Enquete = root.get("Enquete");

			String attributeNum = null;

			if (Enquete instanceof JSONArray) {
				//JSONArray
				System.out.println("配列");
				JSONArray jsonArray = root.getJSONArray("Enquete");


				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Iterator<String> keys = jsonObject.keys();
					///全てのキーを取得

					while(keys.hasNext()){
						String key = keys.next();
						System.out.println(key);

						System.out.println(jsonObject.get("Id"));
						System.out.println(jsonObject.get("VisitorId"));
						System.out.println(jsonObject.get("DataId"));
						System.out.println(jsonObject.get("HistoryId"));

						if(key.contains("List")) {
							JSONObject AttributeList = jsonObject.getJSONObject(key);

							String key2 = key.replace("List", "");
							String attributeId = key.replace("List", "Id");
							String attributeName = key.replace("List", "Name");
							attributeNum = key2.replace("Attribute", "");

							Object Attribute = AttributeList.get(key2);

							if (Attribute instanceof JSONArray) {

								JSONArray jsonArray2= AttributeList.getJSONArray(key2);

								String answer = null;
								StringBuilder buf = new StringBuilder();
								buf.append("\"");
								for(int j = 0; j < jsonArray2.length(); j++) {
									System.out.println("設問　" + key2);
									System.out.println("回答　" + jsonArray2.getJSONObject(j).get(attributeName));

									if (j > 0){
										buf.append("\r\n");
									}
									buf.append(jsonArray2.getJSONObject(j).get(attributeName));
								}

								buf.append("\"");
								answer = buf.toString();

								//CSVの書き込み
								pw.print(jsonObject.get("Id"));
								pw.print(",");
								pw.print(jsonObject.get("VisitorId"));
								pw.print(",");
								pw.print(jsonObject.get("DataId"));
								pw.print(",");
								pw.print(jsonObject.get("HistoryId"));
								pw.print(",");
								pw.print(attributeNum);
								pw.print(",");
								pw.print(answer);
								pw.println();
							}
							else {
								JSONObject AttributeObj = AttributeList.getJSONObject(key2);
								System.out.println(AttributeObj.get(attributeId));
								System.out.println(AttributeObj.get(attributeName));
								pw.print(jsonObject.get("Id"));
								pw.print(",");
								pw.print(jsonObject.get("VisitorId"));
								pw.print(",");
								pw.print(jsonObject.get("DataId"));
								pw.print(",");
								pw.print(jsonObject.get("HistoryId"));
								pw.print(",");
								pw.print(attributeNum);
								pw.print(",");
								pw.print(AttributeObj.get(attributeName));
								pw.println();
							}
						}
						//回答がリスト形式ではない場合
						else if (key.contains("Attribute")){
							attributeNum = key.replace("Attribute", "");
							System.out.println(jsonObject.get(key));
							System.out.println(attributeNum);

							//CSVの書き込み
							pw.print(jsonObject.get("Id"));
							pw.print(",");
							pw.print(jsonObject.get("VisitorId"));
							pw.print(",");
							pw.print(jsonObject.get("DataId"));
							pw.print(",");
							pw.print(jsonObject.get("HistoryId"));
							pw.print(",");
							pw.print(attributeNum);
							pw.print(",");
							pw.print(jsonObject.get(key));
							pw.println();
						}
					}
				}
			} else if (Enquete instanceof JSONObject) {
				//JSONObject
				System.out.println("オブジェクト");
			}
			//test 終了
			// ファイルを閉じる
			pw.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
}