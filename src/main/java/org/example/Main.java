package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.example.bean.CoortinateType;
import org.example.bean.StructureBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        File file = new File("sampleInput.json");
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder data = new StringBuilder();
        String st;
        while ((st = br.readLine()) != null)
            data.append(st);

        Type fooType = new TypeToken<List<StructureBean>>() {
        }.getType();
        List<StructureBean> structureBeanList = gson.fromJson(data.toString(), fooType);

        file = new File("sample.json");
        br = new BufferedReader(new FileReader(file));
        data = new StringBuilder();
        while ((st = br.readLine()) != null)
            data.append(st);
        fooType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> inputMap = gson.fromJson(data.toString(), fooType);
        PdfDocument pdfFoot;
        IEventHandler handler;
        try (PdfReader pdfReader = new PdfReader("Loan Reconciliation Template.pdf")) {
            String outputFilePath = "result.pdf";
            PdfWriter pdfWriter = new PdfWriter(outputFilePath);
            pdfFoot = new PdfDocument(pdfWriter);
            handler = new Background(pdfFoot, pdfReader);
        }
        pdfFoot.addEventHandler(PdfDocumentEvent.START_PAGE, handler);
        Document output = new Document(pdfFoot, PageSize.A4);
        Map<String, Object> map = inputMap.get(0);
        setNormal(structureBeanList, output, map, 1);
        setTable(structureBeanList, output, map, pdfFoot);
        pdfFoot.close();
        br.close();
    }

    private static void setTable(List<StructureBean> structureBeanList, Document output, Map<String, Object> map, PdfDocument pdfFoot) {
        Gson gson = new Gson();
        structureBeanList.stream().filter(t -> {
            if (t.getType().equals(CoortinateType.TABLE))
                return true;
            else
                return false;
        }).forEach(bean -> {
            List<StructureBean> structureBeanListTable = bean.getNestedFields();
            int pageLastCoordinates = bean.getCoordinates().getY();
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            String jsonArray = map.get(bean.getName()).toString();
            JsonArray jsonArrayList = gson.fromJson(jsonArray, JsonArray.class);
            int count = 0;
            int pageNumber = 1;
            for (int i = 0; i < jsonArrayList.size(); i++) {
                JsonElement objects = jsonArrayList.get(i);
                Map<String, String> stringMap = gson.fromJson(objects.getAsJsonObject(), type);
                for (Map.Entry<String, String> maps : stringMap.entrySet()) {
                    List<StructureBean> list = structureBeanListTable.stream().filter(structureBean -> {
                        if (structureBean.getName().equalsIgnoreCase(maps.getKey())) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                    Text author = new Text(maps.getValue());
                    Paragraph p = new Paragraph().setFontSize(8).add(author);
                    int y = list.get(0).getCoordinates().getY() - (count * 20);
                    if (y > pageLastCoordinates) {
                        p.setFixedPosition(pageNumber, list.get(0).getCoordinates().getX(), y, 200);
                        output.add(p);
                    } else {
                        pageNumber++;
                        PdfDocument template = null;
                        try {
                            template = new PdfDocument(new PdfReader("Loan Reconciliation Template.pdf"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        PdfPage page = template.getFirstPage();
                        page.copyTo(pdfFoot);
                        setNormal(structureBeanList, output, map, pageNumber);
                        template.close();
                        count = 0;
                        y = list.get(0).getCoordinates().getY() - (count * 20);
                        p.setFixedPosition(pageNumber, list.get(0).getCoordinates().getX(), y, 200);
                        output.add(p);
                    }
                }
                count++;
            }
        });
    }

    private static void setNormal(List<StructureBean> structureBeanList, Document output, Map<String, Object> map, int pageNumber) {
        structureBeanList.stream().filter(t -> {
            if (t.getType().equals(CoortinateType.NORMAL))
                return true;
            else
                return false;
        }).forEach(bean -> {
            Text author = new Text(map.get(bean.getName()).toString());
            Paragraph p = new Paragraph().setFontSize(8).add(author);
            p.setFixedPosition(pageNumber, bean.getCoordinates().getX(), bean.getCoordinates().getY(), 200);
            output.add(p);

        });
    }

}