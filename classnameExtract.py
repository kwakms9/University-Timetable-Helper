import pandas as pd

file = ".csv"
blankDic = { '구분' : "0" , '학년' : "11",'비고': "*", '소영역': "*", '강의실': "　"} #Nan 제거 후 채움 대상
#df=pd.DataFrame(columns=['구분', '학년', '수강학과', '수강번호', '교과목명', '학점', '시간', '담당교수', '강의시간', '강의실', '소영역', '비고'])
#print(df)
removeList=[]       #삭제한 행 기록(인덱스 오류방지를 위해)
df = pd.DataFrame()

classNamedf = pd.DataFrame(columns=['교과목명','담당교수'])
classList = pd.DataFrame(columns=['교과목명', '담당교수'])
a= pd.DataFrame()
for i in range(6):  #7개의 시간표
    i+=1
    print(i)
    df = pd.read_csv("s"+str(i)+file,encoding="cp949") #시간표읽기
    df.columns=df.columns.str.replace("\r","")
    df.replace("구\r분","구분",inplace=True)
    df.replace("\r", "", inplace=True)
    df["수강학과"] = df["수강학과"].str.replace("\r","")
    df["교과목명"] = df["교과목명"].str.replace("\r", "")
    df["시간"] = df["시간"].fillna("").str.replace("\r", "")
    df["담당교수"] = df["담당교수"].str.replace("\r", "")
    df["강의시간"] = df["강의시간"].str.replace("\r", "|")
    df["강의실"] = df["강의실"].fillna("").str.replace("\r", "|")
    df["소영역"] = df["소영역"].fillna("").str.replace("\r", "")
    df["비고"] = df["비고"].fillna("").str.replace("\r", "")

#print(type(cd.loc[0,'구분']))
#print(cd,"asdfasdf")
#print(cd.loc[28,['구분']],"$$$$$$$",cd.loc[28,['구분']].index)

    ndf = df.fillna(blankDic)  # Nan 제거
    for j in range(df.__len__()):      #필요없이 있는 컬럼명 제거
        if df.loc[j,['구분']][0] =='구\r분' or df.loc[j,['구분']][0]=='구분':
            ndf.drop(df.index[j], inplace=True)
            removeList.append(j)        #삭제된 행 기록
            print(j,"제거---"+str(i)+".csv파일")
           # print(ndf.loc[58, ['구분'][0]],"+++++++++++++++++++++++++++++")

    print("-----------------------------------------컬럼명 제거-----------------------------")
    firstDiv =firstDiv = df.loc[0, ["구분"]][0]
    for k in range(1,df.__len__()):     #구분 빈칸 채우기

        if k in removeList:              #삭제된 목록이 있을 경우 넘어가기
            continue
        if firstDiv == '0':
            firstDiv = df.loc[k - 1, ["구분"]][0]

        #print("============================================", ndf.loc[k, ["교과목명"]])
        if ndf.loc[k,["구분"]][0] == "0": #구분이(공통,균형) nan일경우 상위 구분으로 입력
            ndf.loc[k, ["구분"]] = firstDiv
        else:                               #상위 구분이 변경될 경우 변경
            firstDiv = ndf.loc[k,["구분"]][0]

    firstGrade = df.loc[0,["학년"]][0]
    for k in range(1,df.__len__()):     #학년 빈칸 채우기
        if k in removeList:             #삭제된 목록이 있을 경우 넘어가기
            continue
        if firstGrade =='11':
            firstGrade = df.loc[k - 1, ["구분"]][0]
        #print("============================================", ndf.loc[k, ["교과목명"]])
        if ndf.loc[k,["학년"]][0] == "11": #구분이(공통,균형) nan일경우 상위 구분으로 입력
            ndf.loc[k, ["학년"]] = firstGrade
        else:                               #상위 구분이 변경될 경우 변경
            firstGrade = ndf.loc[k,["학년"]][0]
        print(ndf.loc[k])
    if i ==1:
        classNamedf = ndf.copy()

    ##쓰던거classNamedf=pd.merge(classNamedf,ndf, on=['구분','교과목명','담당교수'],how="outer")
    classNamedf = classNamedf.append(ndf)
    #classNamedf.append([classNamedf,ndf],sort=False)

    #ndf.to_csv(str(i)+file, header=True, index=False, encoding='euc-kr')
    ndf.to_csv("sheduleCSV"+str(i)+file, header=True, index=False, encoding='euc-kr')  #파일마다 정리?본 저장
classList['교과목명'] = classNamedf['교과목명']
classList['담당교수'] = classNamedf['담당교수']
classList = classList.drop_duplicates(['교과목명', '담당교수'])
classList.to_csv("classList21-2" + file, header=True, index=False, encoding='euc-kr')   #교과목 교수만 추출
print('완료!')

'''   
b = pd.read_csv("ttt과연.csv",encoding="cp949")
b.columns=b.columns.str.replace("\r","")
blankDic = { '구분' : 0 , '학년' : 11,'비고': ""}

print(b.fillna(blankDic)[:30])
#print(b.loc[26,'구분']==None)

cd = pd.read_csv("0"+file,encoding="cp949")
cd.columns=cd.columns.str.replace("\r","")
df = pd.DataFrame(cd)
cd["교과목명"] = cd["교과목명"].str.replace("\r","")
cd["담당교수"] = cd["담당교수"].str.replace("\r","")

df=pd.DataFrame()
df.columns()

for i in range(1,2):
    data = pd.read_csv(str(i)+file,encoding="cp949")
    data.columns=data.columns.str.replace("\r","")
    print("sdf")
    data["교과목명"] = data["교과목명"].str.replace("\r","")
    data["담당교수"] = data["담당교수"].str.replace("\r","")
    print(data['담당교수'])
    #df.append(data)
    #print(df)
    #pd.concat([df,data], ignore_index=True)
    pd.merge(df,data)


#print(df)
test=df.drop_duplicates(['교과목명','담당교수'])
print(test.loc[:,['교과목명','담당교수']])
df.to_csv("ttttest.csv",header=False, index=False, encoding='euc-kr')
#print(type(a))
#aa = re.sub("[^ a-zA-Z0-9\n]", "",a)
#print(aa)
#test = lines[7].split(",")[5]
'''
