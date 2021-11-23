from tabula.io import read_pdf, convert_into
'''www.convertfiles.com/ 5,6 링크는 여기서 변환 수정후 사용해야함'''
url1='https://daegu.ac.kr/data/pdf/DG64/2021_2_lecture_07_01.pdf'#"C:/Users/Me/Downloads/시간표/1schedule.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_01.pdf" #공통/균형교양
url2='https://daegu.ac.kr/data/pdf/DG64/2021_2_lecture_07_02.pdf'#"C:/Users/Me/Downloads/시간표/2schedule.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_01.pdf" #자유선택
url3='https://daegu.ac.kr/data/pdf/DG64/2021_2_lecture_07_03.pdf'#"C:/Users/Me/Downloads/시간표/3schedule.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_02.pdf" #일선
url4='https://daegu.ac.kr/data/pdf/DG64/2021_2_lecture_07_04.pdf'#"C:/Users/Me/Downloads/시간표/4schedule.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_04.pdf"  #연계전공
url5='https://daegu.ac.kr/data/pdf/DG64/2021_2_lecture_07_05.pdf'#"C:/Users/Me/Downloads/시간표/5schedule-1.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_04.pdf"  #연계전공
url6='https://daegu.ac.kr/data/pdf/DG64/2021_2_lecture_07_07.pdf'#"C:/Users/Me/Downloads/시간표/5schedule-2.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_05.pdf"   #창의융복합
#url7="C:/Users/Me/Downloads/시간표/6schedule.pdf" #"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_06.pdf"   #마이크로전공 안함
#url8="C:/Users/Me/Downloads/시간표/7schedule.pdf"#"https://daegu.ac.kr/data/pdf/DG64/2021_1_lecture_07_07.pdf"   #전공


urlList=[url1,url2,url3,url4,url5,url6]

try:
    for i,url in enumerate(urlList):
        i+=1
        df= read_pdf(url, pages="2-")
        convert_into(url, "s"+str(i)+".csv", lattice=True, output_format='csv', pages='all')

        print(df[0])
except Exception as e:
    print(e)

'''
from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument

fp = open(url6, 'rb')
parser = PDFParser(fp)
doc = PDFDocument(parser)
fp.close()
print(doc.info)

from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfpage import PDFPage
from pdfminer.pdfpage import PDFTextExtractionNotAllowed
from pdfminer.pdfinterp import PDFResourceManager
from pdfminer.pdfinterp import PDFPageInterpreter
from pdfminer.pdfdevice import PDFDevice
from pdfminer.layout import LAParams
from pdfminer.converter import PDFPageAggregator
import pdfminer

# Open a PDF file.
fp = open(url5, 'rb')

# Create a PDF parser object associated with the file object.
parser = PDFParser(fp)

# Create a PDF document object that stores the document structure.
# Password for initialization as 2nd parameter
document = PDFDocument(parser)

# Check if the document allows text extraction. If not, abort.
if not document.is_extractable:
    raise PDFTextExtractionNotAllowed

# Create a PDF resource manager object that stores shared resources.
rsrcmgr = PDFResourceManager()

# Create a PDF device object.
device = PDFDevice(rsrcmgr)

# BEGIN LAYOUT ANALYSIS
# Set parameters for analysis.
laparams = LAParams()

# Create a PDF page aggregator object.
device = PDFPageAggregator(rsrcmgr, laparams=laparams)

# Create a PDF interpreter object.
interpreter = PDFPageInterpreter(rsrcmgr, device)

def parse_obj(lt_objs):

    # loop over the object list
    for obj in lt_objs:

        # if it's a textbox, print text and location
        if isinstance(obj, pdfminer.layout.LTTextBoxHorizontal):
            if obj.bbox[0]!=45:#url1 -47
                print("%6d, %6d, %s" % (obj.bbox[0], obj.bbox[1], obj.get_text().replace('\n', '_')))

        # if it's a container, recurse
        elif isinstance(obj, pdfminer.layout.LTFigure):
            parse_obj(obj._objs)

# loop over all pages in the document
for page in PDFPage.create_pages(document):

    # read the page into a layout object
    interpreter.process_page(page)
    layout = device.get_result()

    # extract text from this object
    parse_obj(layout._objs)
fp.close()


import PyPDF2
from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfpage import PDFPage
from io import StringIO
import os

fp = open(url5, 'rb')
total_page=PyPDF2.PdfFileReader(fp).numPages
print(total_page)#shineedayes.tistory.com/48
'''