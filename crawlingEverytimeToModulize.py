from selenium import webdriver
import os
import pandas as pd
import time
import datetime

###everytime의 과목별 링크까지 가져오도록###

def progress(length,index):
    print("-----",str(round(index/length*100,1))+"% progress... -----")

def crawlingScoreAndLink():
    options = webdriver.ChromeOptions()
    #options.add_argument('headless')
    options.add_argument('User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Safari/537.36')
    #options.add_argument('--incognito')  # 시크릿
    options.add_argument('--no-sandbox')  # 애랑
    options.add_argument('--no-sandbox')    #애랑
    options.add_argument('--disable-dev-shm-usage')#얘 앗!,이런! 문제 방지용?
    driver = webdriver.Chrome("chromedriver", options=options)
    url = "https://everytime.kr/login"   #main
    driver.get(url)
    #driver.implicitly_wait(10)
    #wait = WebDriverWait(driver, 10) #Explicitly wait 명시적으로 어떤 조건이 성립했을 때까지
    #element = wait.until(EC._find_element((By.CLASS_NAME, 'lecture') or EC._find_element((By.CLASS_NAME, 'empty'))))

    driver.find_element_by_css_selector("#container > form > p:nth-child(1) > input").send_keys("에브리타임 아이디")
    #p=input("비밀번호: ")
    driver.find_element_by_css_selector("#container > form > p:nth-child(2) > input").send_keys(p)
    driver.find_element_by_css_selector("#container > form > p.submit > input").click()
    os.system("cls")

    for i in range(100):
        print()
    #scoreList = []
    #input("wait")
    driver.get("https://everytime.kr/lecture")

    df= pd.read_csv("classList21-2.csv",encoding="cp949") #과목,교수이름 읽기
    startTime = datetime.datetime.now()# 시작시간
    coursDic = df.to_dict('records')    #사전으로 변환
    errorList =[]
    for i ,dicset in enumerate(coursDic):
        className = dicset.get('교과목명')
        prof = dicset.get('담당교수')
        score = ""
        #className ="DU-HEART세미나(1)"
        #prof = "류미경"
        #input("test")
        if prof == None:
            prof = "미정"

        try:
            driver.get("https://everytime.kr/lecture/search/"+str(prof))
        except Exception as e:
            print("에러발생",e)
            print(className,prof)
            driver.implicitly_wait(5)
            driver.refresh()    #새로고침
            try:
                driver.get("https://everytime.kr/lecture/search/" + str(prof))
            except Exception  as e:
                errorList.append([className,prof])
                continue
            #break
        #element = wait.until(EC.presence_of_element_located((By.XPATH, '//*[@id="container"]/div/a[1]')))
        driver.implicitly_wait(5)
        try:
            j=1
            time.sleep(0.7)
            print(className,prof)
            while True:
                try:    #인덱스초과로 에러 뜰 경우
                    queryClassName = driver.find_element_by_xpath('//*[@id="container"]/div/a['+str(j)+']/h3/p[1]').text

                except Exception as e:
                    print( "없음", e)
                    print("except과목없음")
                    score="0"    #컬럼 밀림방지
                    coursUrl = "empty"  # 컬럼 밀림방지
                    queryClassName = "미정"
                    break


                #print("find..."+className,prof,"->",queryProfName)
                if queryClassName == className :   #해당 강좌 확인
                    driver.find_element_by_css_selector("#container > div > a:nth-child(" + str(j) + ")").click()       #move page
                    coursUrl = driver.current_url
                    time.sleep(0.7)
                    score = driver.find_element_by_css_selector("#container > div.side.article > div.rating > div.rate > span > span.value").text   #별점

                    print("-"*20)
                    print(queryClassName)
                    #print(queryProfName)
                    print(score)
                    print(coursUrl)
                    print("-"*20)
                    progress(df.__len__(),i)
                    break
                j+=1
        except Exception as e:
            print(className,prof,"없음", e)
        df.loc[i, ["평점"]] = score      #강의평점 입력
        df.loc[i, ["link"]] = coursUrl  #과목링크

    df.to_csv("courseUpdateData.csv", header=True, index=False, encoding='euc-kr')
    print("complete!")
    finishTime = datetime.datetime.now()
    print(startTime,'\n',finishTime)
    print("*"*20)
    print("errorList:\n",errorList)
    driver.quit()




