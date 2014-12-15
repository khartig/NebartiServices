#! /usr/bin/perl -w

 use warnings; use strict;
 my $url = shift or die "usage: $0 <url>\n I need a url to be useful!";

 use WWW::Mechanize;
 my $mech = WWW::Mechanize->new();
 $mech->get( $url );
 my $html = $mech->content();

 use HTML::TreeBuilder::XPath; 
 my $document = HTML::TreeBuilder::XPath->new_from_content( $html );

 print qq{<a href="$url">},
     $document->findvalue( q{//html//title} ),
     qq{</a>};