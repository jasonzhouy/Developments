import requests
from bs4 import BeautifulSoup
import datetime
import time
import random
import os
import re
import fake_useragent


def getHeader():
    location = os.getcwd() + '/fake_useragent.json'
    ua = fake_useragent.UserAgent(path=location)
    return ua.random


# 设置请求头 避免反爬虫
headers = {

    "User-Agent": getHeader()

}
# 代理ip地址
proxy_ip_list = "https://www.kuaidaili.com/free/inha/"
# 输出文件的保存路径
base_path = "output/"
# 默认单个输出文件大小为128MB（与HDFS对应）
base_file_size = 128 * 1024 * 1024
# 当前写入的文件，第一次执行任务时才会赋值
current_write_file = ""
# 当前写入文件的序号，即是第几份文件,默认从第一份开始
current_write_number = 1
# 当前启用的日志文件
current_logs = ""
# 错误页面内容
error_page_content = ""

# 城市信息  上海 广州 深圳 杭州 苏州 武汉 南京 北京 成都
city_info = ["c101020100", "c101280100", "c101280600", "c101210100", "c101190400", "c101200100", "c101190100",
             "c101010100", "c10127010"]

# 岗位信息
#   技术 p100 + xxx  101到302
#   产品 p110 + xxx  101到108
#
post_info = ["p100", "p110", "p120", "130"]
post_info_page = [302, 108, 121, 110]


# 获取高匿IP列表
def get_ip_list(temp, headers):
    ip_list = []
    for i in range(1, 10):
        web_data = requests.get(temp + str(i) + "/", headers=headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        for j in range(1, len(ips)):
            ip_info = ips[j]
            tds = ip_info.find_all('td')
            ip_list.append(tds[0].text + ':' + tds[1].text)
    # print(ip_list)
    return ip_list


# 获取随机IP地址
def get_random_ip(ip_list):
    proxy_list = []
    for ip in ip_list:
        proxy_list.append('http://' + ip)
    proxy_ip = random.choice(proxy_list)
    print(proxy_ip)
    proxies = {'http': proxy_ip}
    return proxies


# 获取文件大小
def getFileSize(path):
    try:
        size = os.path.getsize(path)
        return formatSize(size)
    except Exception as err:
        print(err)


# 判断文件大小是否符合要求
def formatSize(bytes):
    try:
        bytes = float(bytes)
    except:
        print("传入的字节格式不对")
        return "Error"

    if bytes < base_file_size:
        return True
    else:
        return False


# 获取当前文件
def getCurrentFile():
    dateprefix = datetime.datetime.now().strftime('%m%d')
    # print(dateprefix)
    global current_write_file
    global current_write_number
    global current_logs
    current_write_file = base_path + dateprefix + "output-" + str(current_write_number) + ".csv"

    current_logs = base_path + "logs/" + dateprefix + ".logs"

    if not os.path.exists(current_logs):
        os.system(r"touch {}".format(current_logs))

    while os.path.exists(current_write_file):
        if (formatSize(getFileSize(current_write_file))):
            break
        else:
            f = open(current_write_file, "r")
            lines = f.readlines()
            f.close()
            f2 = open(current_logs, "a+")
            f2.write(str(datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')) + ":   " +
                     current_write_file + "一共写入了" + lines + "行数据\n")
            f2.close()
            current_write_file_number = current_write_number + 1
            current_write_file = dateprefix + "output-" + current_write_file_number + ".csv"

    if not os.path.exists(current_write_file):
        os.system(r"touch {}".format(current_write_file))

    ff = open(current_logs, "a+")
    ff.write(str(datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')) + ":   开始写" + current_write_file + "了.\n")
    ff.close()


# 获取网页内容
def getUrlContent():
    ip_list = get_ip_list(proxy_ip_list, headers=headers)

    for i in city_info:
        # 判断当前写入文件是否过大
        getCurrentFile()
        # print("当前写入的文件是:" + current_write_file)
        f = open(current_write_file, "a+", encoding='utf-8')  # 设置文件对象
        fl = open(current_logs, "a+", encoding="utf-8")
        for j in post_info:

            for k in range(101, post_info_page[post_info.index(j)]):

                # 做判断,避免重复请求不存在的页面，触发Boss的异常检测
                temp = requests.get(url="https://www.zhipin.com/" + i + "-" + str(j) + str(k) + "/?", headers=headers)
                soupTemp = BeautifulSoup(temp.text, 'lxml')
                inputTemp = soupTemp.find_all('div', attrs={'class': 'job-primary'})
                if judgeRepated(inputTemp):
                    continue

                for m in range(1, 8):
                    url = "https://www.zhipin.com/" + i + "-" + str(j) + str(k) + "/?page=" + str(
                        m) + "&ka=page-" + str(
                        m)
                    print(url)
                    proxies = get_random_ip(ip_list)

                    # 做判断,避免重复请求不存在的页面，触发Boss的异常检测
                    web_data = requests.get(url, proxies=proxies, headers=headers)
                    soup = BeautifulSoup(web_data.text, 'lxml')
                    input = soup.find_all('div', attrs={'class': 'job-primary'})

                    # 如果页面被重定向，说明这个页面不存在数据了，跳过
                    if judgeRepated(input):
                        break

                    # 如果没有被重定向，则获得网页内的信息
                    getData(web_data, f, fl)
                    # 休眠一段时间，避免同一时间段内高频请求被拉黑
                    time.sleep(random.randint(2, 10))

    f.flush()
    fl.close()
    f.close()


# 解析网页数据，拿到所需数据集写入csv文件
def getData(web_data, f, fl):
    soup = BeautifulSoup(web_data.text, 'lxml')
    job_primary = soup.find_all('div', attrs={'class': 'job-primary'})
    # print(job_primary)
    count = 0
    for i in job_primary:
        job_name = i.find("div", attrs={'class': 'job-title'}).text
        job_salary = i.find("span", attrs={'class': 'red'}).text
        salary_search = re.search(r'([0-9]{1,2})-([0-9]{1,2})', job_salary)
        job_salary_min = "0"
        job_salary_max = "0"
        if salary_search:
            job_salary_min = salary_search.group(1)
            job_salary_max = salary_search.group(2)
        job_mix = i.find('p').text
        # print(job_mix)
        # 截取我需要的地名，注意，[0:2]只截取了两个字符
        job_address = str(job_mix)[0:2]
        job_experience = "不限"
        job_grade = "不限"
        searchStr = re.search(
            r'([0-9\-]+[\u5e74][\u4ee5\u5185]*|[\u7ecf\u9a8c\u4e0d\u9650]{4}|[\u5e94\u5c4a\u751f]{3})([\u4e00-\u9fa5]+)',
            job_mix)

        # 正则获取我需要的工作经验和学历
        if searchStr:
            job_experience = searchStr.group(1)
            job_grade = searchStr.group(2)

        # print(job_experience)
        job_company = i.find('div', attrs={'class': 'company-text'}).find('h3', attrs={'class': 'name'}).find('a').text
        job_scale = i.find('div', attrs={'class': 'company-text'}).find('p').text
        job_data = job_address + " " + job_name + " " + job_company + " " + job_grade + " " + job_experience + " " + job_salary_min + " " + job_salary_max + " " + job_scale
        f.write(job_data)
        count = count + 1
        f.write("\n")
        # print(job_data)
    fl.write(str(datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')) + " 写入了" + str(count) + "\n")


def getErrorPage():
    global error_page_content
    web_data = requests.get("https://www.zhipin.com/job_detail/?ka=header-job", headers=headers)
    soup = BeautifulSoup(web_data.text, 'lxml')
    error_page_content = soup.find_all('div', attrs={'class': 'job-primary'})


def judgeRepated(web_content):
    if error_page_content == web_content:
        return True
    else:
        return False


def mainAction():
    getErrorPage()
    getUrlContent()


mainAction()
