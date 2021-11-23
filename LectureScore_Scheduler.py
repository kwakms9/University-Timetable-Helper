import schedule
import cx_Oracle
import crawlingEverytimeToModulize as crawl
import pandas as pd

def progress(length,index): #진행도
    print("-----",str(round(index/length*100,1))+"% progress... -----")

def scheduleRun():
    crwalScore()
    input("stop")
    insertData()

def crwalScore(): #평점 새로 가져오기
    crawl.crawlingScoreAndLink()

def insertData():   #새로가져온 데이터 삽입하기
    data=pd.read_csv("courseUpdateData.csv",encoding="cp949") #데이터 불러오기
    lectureList=data.to_dict('records')
    connection = cx_Oracle.connect('test', 'test', 'localhost:1522/orcl')
    cursor = connection.cursor()
    cursor.execute("DELETE FROM COURSE")  #기존데이터 제거
    connection.commit()

    for i, lecture in enumerate(lectureList):
        classname = lecture.get("교과목명")
        professor= lecture.get("담당교수")
        score= lecture.get("평점")
        link=lecture.get("link")
        print(classname, professor, score,link)
        cursor.execute("INSERT INTO COURSE(CLASSNAME, PROFESSOR, SCORE, LINK) \
        VALUES(:1,:2,:3,:4)", \
        (classname, professor, score,link))   #데이터 삽입

        progress(lectureList.__len__(),i)   #진행도
    connection.commit()
    cursor.close()
    connection.close()

schedule.every().monday.do(scheduleRun) #매주 월요일 업데이트 스케줄러 작동
#scheduleRun()
while True:
   schedule.run_pending()

