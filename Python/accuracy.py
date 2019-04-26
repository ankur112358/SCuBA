from collections import Counter
import operator
with open('/home/zoro/Downloads/row.txt') as f:
    lines = f.read().splitlines()
with open('/home/zoro/Downloads/col.txt') as f:
    lines123 = f.read().splitlines()

print len(lines)
with open('/home/zoro/Downloads/result.txt') as f:
    cluster = f.read().splitlines()
print len(cluster)
for item in cluster:
	if len(item)==0:
		cluster.remove(item)
print len(cluster)
counter=0
correct = 0
size=0
largest_feature=''
l_f=0
l_d=0
l_t=0
largest_doc=''
largest_total=''
docs=[]
features=[]
for a in cluster:
	a=a.split(' : ')
	b=a[-1].split(' ')
	c=a[0].split(' ')
	docs.extend(b)
	features.extend(c)
	if len(c)>l_f:
		l_f=len(c)
		largest_feature=a
	if len(b)>l_d:
		l_d=len(b)
		largest_doc=a
	if len(b)==5 and len(c)==5:
		l_t=len(b)+len(c)
		largest_total=a
	size=size+len(b)
	aa=[]
	for item in b:
		aa.append(lines[int(item)-1])
	temp=dict(Counter(aa))
	sorted_x = sorted(temp.items(), key=operator.itemgetter(1))
	correct = correct + sorted_x[-1][1]
	if len(sorted_x) == 1:
		pass
	else:
		for i in range(len(sorted_x) -1):
			counter=counter + sorted_x[i][1]

print((docs[0]))
print(features[0])
d=set(docs)
f=set(features)
print ('doc',len(lines)- len(list(d)))
print('features', len(lines123)-len(list(f)))


print counter, correct, size, counter+correct
print len(cluster)

print 1-float(counter)/ size
print largest_doc
print largest_feature
print largest_total
temp=largest_doc[0].split(' ')
abc=''
de=''
for item in temp:
	abc=abc+" "+lines123[int(item)-1]
abc=abc+' : '
temp=largest_doc[1].split(' ')
for item in temp:
	abc=abc+" "+lines[int(item)-1]
print abc
print '\n'

temp=largest_feature[0].split(' ')
abc=''
for item in temp:
	abc=abc+" "+lines123[int(item)-1]
abc=abc+' : '
temp=largest_feature[1].split(' ')
for item in temp:
	abc=abc+" "+lines[int(item)-1]
print abc
print '\n'

temp=largest_total[0].split(' ')
abc=''
for item in temp:
	abc=abc+" "+lines123[int(item)-1]
abc=abc+' : '
temp=largest_total[1].split(' ')
for item in temp:
	abc=abc+" "+lines[int(item)-1]
print abc

