clear sub1;
incr sub1;
incr sub1;
incr sub1;
incr sub1;
incr sub1;
clear sub2;
incr sub2;
incr sub2;
incr sub2;
clear temp;
while sub2 not 0 do;
	decr sub1;
	decr sub2;
	incr temp;
end;
while temp not 0 do;
	incr sub2;
	decr temp;
end;